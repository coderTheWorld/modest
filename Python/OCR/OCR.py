import pytesseract
from PIL import Image


img = Image.open("F:\\SourceCode\\Python\\OCR\\Capture.PNG")
code = pytesseract.image_to_string(img, lang="chi_sim")
print(code)
try:
    with open(
        "F:\\SourceCode\\Python\\OCR\\1.txt".encode("utf-8"), "w", encoding="utf-8"
    ) as f:
        f.write(code)
except IOError as e:
    print("FILI error:", str(e))
