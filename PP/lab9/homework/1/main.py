import subprocess
import re
import os
from abc import ABC, abstractmethod

# ---- Command Pattern ----
class Command(ABC):
    @abstractmethod
    def execute(self, file_path):
        pass

class PythonCommand(Command):
    def execute(self, file_path):
        try:
            result = subprocess.run(["python", file_path], capture_output=True, text=True)
            return result.stdout or result.stderr
        except Exception as e:
            return f"Error executing Python code: {e}"

class JavaCommand(Command):
    def execute(self, file_path):
        try:
            # Extract class name from content for Java
            with open(file_path, 'r') as f:
                content = f.read()
            
            class_match = re.search(r'class\s+(\w+)', content)
            if not class_match:
                return "Error: Could not find Java class name"
            
            class_name = class_match.group(1)
            directory = os.path.dirname(file_path)
            
            # Create a temporary symbolic link with .java extension
            java_file = os.path.join(directory, f"{class_name}.java")
            
            # Copy content to .java file
            with open(java_file, 'w') as f:
                f.write(content)
            
            # Compile and run Java code
            compile_result = subprocess.run(["javac", java_file], capture_output=True, text=True)
            if compile_result.returncode != 0:
                return f"Java compilation error: {compile_result.stderr}"
            
            # Run the compiled class file
            run_result = subprocess.run(["java", "-cp", directory, class_name], capture_output=True, text=True)
            
            # Clean up the temporary files
            try:
                os.remove(java_file)
                os.remove(os.path.join(directory, f"{class_name}.class"))
            except:
                pass
                
            return run_result.stdout or run_result.stderr
        except Exception as e:
            return f"Error executing Java code: {e}"

class KotlinCommand(Command):
    def execute(self, file_path):
        try:
            # Create a temporary file with .kt extension
            directory = os.path.dirname(file_path)
            basename = os.path.basename(file_path)
            kotlin_file = os.path.join(directory, f"{basename}.kt")
            
            # Copy content to .kt file
            with open(file_path, 'r') as src, open(kotlin_file, 'w') as dest:
                dest.write(src.read())
            
            # Compile and run Kotlin code
            result = subprocess.run(["kotlin", kotlin_file], capture_output=True, text=True)
            
            # Clean up the temporary file
            try:
                os.remove(kotlin_file)
            except:
                pass
                
            return result.stdout or result.stderr
        except Exception as e:
            return f"Error executing Kotlin code: {e}"

class BashCommand(Command):
    def execute(self, file_path):
        try:
            # Make the script executable
            subprocess.run(["chmod", "+x", file_path], capture_output=True, text=True)
            
            # Run the Bash script
            result = subprocess.run(["bash", file_path], capture_output=True, text=True)
            return result.stdout or result.stderr
        except Exception as e:
            return f"Error executing Bash code: {e}"

# ---- Chain of Responsibility Pattern ----
class FileHandler(ABC):
    def __init__(self):
        self._next_handler = None
    
    def set_next(self, handler):
        self._next_handler = handler
        return handler
    
    def handle(self, file_path):
        # Read file content for analysis
        try:
            with open(file_path, "r") as file:
                content = file.read()
                
            if self.can_handle(content):
                return self.process(file_path)
            elif self._next_handler:
                return self._next_handler.handle(file_path)
            return "Unknown file type"
        except Exception as e:
            return f"Error handling file: {e}"
    
    @abstractmethod
    def can_handle(self, file_content):
        pass
    
    @abstractmethod
    def process(self, file_path):
        pass

class PythonHandler(FileHandler):
    def can_handle(self, file_content):
        # Check for Python-specific characteristics
        python_indicators = [
            re.search(r'import\s+(\w+)', file_content),
            re.search(r'from\s+(\w+)\s+import', file_content),
            re.search(r'def\s+\w+\s*\(.*\):', file_content),
            re.search(r'if\s+__name__\s*==\s*[\'"]__main__[\'"]:', file_content),
            re.search(r'print\s*\(', file_content),
            "#!/usr/bin/env python" in file_content,
            "#!/usr/bin/python" in file_content
        ]
        
        # Count how many indicators are present
        indicator_count = sum(1 for ind in python_indicators if ind)
        
        # Require at least 2 Python indicators to be reasonably confident
        return indicator_count >= 2
    
    def process(self, file_path):
        print("Python file detected")
        command = PythonCommand()
        return command.execute(file_path)

class JavaHandler(FileHandler):
    def can_handle(self, file_content):
        # Check for Java-specific characteristics
        java_indicators = [
            re.search(r'public\s+class\s+\w+', file_content),
            re.search(r'public\s+static\s+void\s+main', file_content),
            re.search(r'System\.out\.println', file_content),
            re.search(r'import\s+java\.', file_content)
        ]
        
        # Count how many indicators are present
        indicator_count = sum(1 for ind in java_indicators if ind)
        
        # Require at least 2 Java indicators to be reasonably confident
        return indicator_count >= 2
    
    def process(self, file_path):
        print("Java file detected")
        command = JavaCommand()
        return command.execute(file_path)

class KotlinHandler(FileHandler):
    def can_handle(self, file_content):
        # Check for Kotlin-specific characteristics
        kotlin_indicators = [
            re.search(r'fun\s+\w+\s*\(', file_content),
            re.search(r'val\s+\w+', file_content),
            re.search(r'var\s+\w+', file_content),
            re.search(r'fun\s+main\s*\(', file_content),
            re.search(r'when\s*\(', file_content),
            re.search(r'import\s+kotlin\.', file_content)
        ]
        
        # Count how many indicators are present
        indicator_count = sum(1 for ind in kotlin_indicators if ind)
        
        # Require at least 2 Kotlin indicators to be reasonably confident
        return indicator_count >= 2
    
    def process(self, file_path):
        print("Kotlin file detected")
        command = KotlinCommand()
        return command.execute(file_path)

class BashHandler(FileHandler):
    def can_handle(self, file_content):
        # Check for Bash-specific characteristics
        bash_indicators = [
            re.search(r'#!/bin/(ba)?sh', file_content),
            re.search(r'#!/usr/bin/(ba)?sh', file_content),
            re.search(r'echo\s+["\']', file_content),
            re.search(r'\$\w+', file_content),  # Variables like $PATH
            re.search(r'if\s+\[\s+.*\s+\]', file_content),
            re.search(r'for\s+\w+\s+in', file_content)
        ]
        
        # Count how many indicators are present
        indicator_count = sum(1 for ind in bash_indicators if ind)
        
        # Require at least 2 Bash indicators to be reasonably confident
        return indicator_count >= 2
    
    def process(self, file_path):
        print("Bash file detected")
        command = BashCommand()
        return command.execute(file_path)

def process_file(file_path, handler_chain, output_file="execution_results.txt"):
    # Create a separator line for the file
    separator = f"\n{'-' * 40}\n"
    
    # Process the file through the chain
    result = handler_chain.handle(file_path)
    
    # Write the results to the output file
    with open(output_file, "a") as f:
        f.write(separator)
        f.write(f"Processing file: {file_path}\n\n")
        f.write("Execution output:\n")
        f.write(result if result else "[No output]")
        f.write("\n")
    

if __name__ == "__main__":
    # Set up the chain of responsibility
    python_handler = PythonHandler()
    java_handler = JavaHandler()
    kotlin_handler = KotlinHandler()
    bash_handler = BashHandler()
    
    python_handler.set_next(java_handler).set_next(kotlin_handler).set_next(bash_handler)
    
    # Get the current directory
    current_dir = os.getcwd()
    print(f"Scanning files in: {current_dir}")
    
    # Get all files without extensions in the current directory
    files_without_extension = []
    for file in os.listdir(current_dir):
        file_path = os.path.join(current_dir, file)
        if os.path.isfile(file_path) and '.' not in file:
            files_without_extension.append(file_path)
    
    if not files_without_extension:
        print("No files without extensions found in the current directory.")
        exit()
    
    # Process each file
    print(f"Found {len(files_without_extension)} files without extensions.")
    for file_path in files_without_extension:
        process_file(file_path, python_handler)
