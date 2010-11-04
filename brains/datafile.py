import random

class DataFile(object):
    """Reads csv files containing class, feature lists. Streams to 
    save memory."""

    SEP = ","

    def __init__(s, path):
        s.file = open(path, "r")
        s.features = None
        s.count = 0
        for line in s.file:
            if line.find("#") == 0:
                continue
            parts = line.strip().split(DataFile.SEP)
            if s.features == None:
                s.features = parts[1:]
                break

    def points(s):
        for line in s.file:
            parts = line.strip().split(DataFile.SEP)
            klass = parts[0]
            data = zip(s.features, parts)
            s.count += 1
            yield(klass, dict(data))

    def split(s, fraction):
        allp = [p for p in s.points()]
        random.shuffle(allp)
        split = len(allp) / fraction
        return (allp[:split], allp[split:])
        
