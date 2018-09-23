import sys

try:
    with open(".\《八十一数吉凶佩带琥珀守护神八卦吉祥笔画划表》.txt", "r", encoding="UTF-8") as f:
        line = f.readlines()
        for num in sys.argv[1:]:
            print(num, end=" ")
            tail = int(num) % 81
            print(tail)
            print(line[tail - 1], end="")
except IOError as e:
    print("FILI error:", str(e))
