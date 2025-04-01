# Import Aspose.Words for Python via .NET module
import aspose.words as aw

# Create and save a simple document

# doc = aw.Document("Input.txt")
# doc.save("Output.html")


doc = aw.Document()
builder = aw.DocumentBuilder(doc)
builder.writeln("Hello Aspose.Words for Python via .NET")

doc.save("Output.html")