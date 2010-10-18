class DataFile(object):
    """Reads csv files containing class, feature lists. Streams to 
    save memory."""

    def __init__(s, path):
        s.file = open(path, "r")
        s.features = None
        s.count = 0

    def points(s):
        for line in s.file:
            if line.find("#") == 0:
                continue
            parts = line.split(",")
            if s.features == None:
                s.features = parts[1:]
            else:
                klass = parts[0]
                data = zip(s.features, parts)
                s.count += 1
                yield(klass, data)
