import pytesseract
from PIL import Image


img = Image.open('./Capture.PNG')
code = pytesseract.image_to_string(img,lang='chi_sim')
print(code)
