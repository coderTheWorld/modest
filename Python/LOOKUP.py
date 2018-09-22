import pickle


result = {}
try:
    with open("mydata.pickle", "rb") as myrestoredata:
        result = pickle.load(myrestoredata)
except IOError as e:
    print("FILI error:", str(e))
print(sorted(result.items(), key=lambda item: item[1])[-33:])
