import urllib.request
import urllib.error
import socket
import html.parser
import pickle


result = {}
count = 0
try:
    with open("mydata.pickle", "rb") as myrestoredata:
        result = pickle.load(myrestoredata)
except IOError as e:
    print("FILI error:", str(e))


class MyHTMLPraser(html.parser.HTMLParser):
    def __init__(self):
        html.parser.HTMLParser.__init__(self)
        self.flag = True

    def handle_data(self, data):
        if self.lasttag == "title" and self.flag:
            # print("Data     :", data)
            s = data.split("-")
            s = s[:2]
            # s[0] = s[0].strip()
            s[1] = s[1].split("￥")[1].strip()
            result[str] = float(s[1])
            self.flag = False


def saveMyData():
    try:
        with open("mydata.pickle", "wb") as mysavedata:
            pickle.dump(result, mysavedata)
    except IOError as e:
        print("FILE error:", str(e))


for i in range(10000):
    str = "%04d" % i
    str = "1557730" + str
    if str in result.keys():
        print(str, result[str])
    else:
        url = "http://mobile.9om.com/?lang=ch-zn&q=" + str
        print(url)
        req = urllib.request.Request(
            url,
            headers={
                "HOST": "mobile.9om.com",
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0",
                "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language": "en-US,en;q=0.5",
                # "Accept-Encoding": "gzip, deflate", 这条信息代表本地可以接收压缩格式的数据，而服务器在处理时就将大文件压缩再发回客户端，IE在接收完成后在本地对这个文件又进行了解压操作。出错的原因是因为你的程序没有解压这个文件，所以删掉这行就不会出现问题了
                "Referer": "http://mobile.9om.com/",
                "Connection": "keep-alive",
                "Upgrade-Insecure-Requests": "1",
                "Pragma": "no-cache",
                "Cache-Control": "no-cache",
            },
            method="GET",
        )

        try:
            with urllib.request.urlopen(req, timeout=33) as f:
                print("status", f.status, f.reason)
                res = f.read().decode("utf-8")
                parser = MyHTMLPraser()
                parser.feed(res)
                parser.close()
        except urllib.error.HTTPError as e:
            print("Server could not fulfill the request,erro code", e.code, e.info())
        except urllib.error.URLError as e:
            print("We failed to reach the server:", e.reason)
        except socket.timeout:
            print("Time out")
            # saveMyData() 如果超时你强制终止程序，会导致爬虫结果文件数据未写入变成空文件
            count = 0

        count += 1

        if count % 9 == 0:
            saveMyData()
            count = 0

print(sorted(result.items(), key=lambda item: item[1])[-33:])
