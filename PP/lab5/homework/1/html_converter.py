import os
import sys
import aspose.words as aw

import sysv_ipc

from pathlib import Path
from PyQt5.QtWidgets import QWidget, QApplication, QFileDialog
from PyQt5.uic import loadUi
from PyQt5 import QtCore


def debug_trace(ui=None):
    from pdb import set_trace
    QtCore.pyqtRemoveInputHook()
    set_trace()
    # QtCore.pyqtRestoreInputHook()


class HTMLConverter(QWidget):
    ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

    def __init__(self):
        super(HTMLConverter, self).__init__()
        ui_path = os.path.join(self.ROOT_DIR, 'html_converter.ui')
        loadUi(ui_path, self)
        self.browse_btn.clicked.connect(self.browse)
        self.convert_btn.clicked.connect(self.convert)
        self.file_path = None
        
        self.key = sysv_ipc.ftok("message_queue_name", ord('B'))

        self.mq = sysv_ipc.MessageQueue(self.key, sysv_ipc.IPC_CREAT)
        print(f"{self.key} {self.mq}")

    def browse(self):
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        file, _ = QFileDialog.getOpenFileName(self,
                                              caption='Select file',
                                              directory='',
                                              filter="Text Files (*.txt)",
                                              options=options)
        if file:
            self.file_path = file
            self.path_line_edit.setText(file)
            print(file)

    def convert(self):
        file = open(self.file_path, "r")

        content_lines = file.readlines()
        file_name = Path(self.file_path).stem

        html_content = f"""<!DOCTYPE html>\n<html lang="en">\n<title>{file_name}</title>\n"""

        for line in content_lines:
            html_content = html_content + f"<p>{line}</p>\n"

        html_content = html_content + "</html>"
       
        #Send through the message queue
        self.mq.send(html_content.encode('utf-8'), type=1)
        print("The content has been sent! Waiting for processing...")

        file.close()



if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = HTMLConverter()
    window.show()
    sys.exit(app.exec_())