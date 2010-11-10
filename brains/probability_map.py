class ProbabilityMap(object):
    "Maps counts features + values => classes."
    def __init__(s, points = []):
        s.count = 0
        s.pc = {} # class probability
        s.pf = {} # feature probability
        s.pfc = {} # feature-class probabiltiy
        for p in points:
            s.update(p[0], p[1])

    def update(s, klass, features):
        "Add a new sample of class klass with features to the map."
        assert klass and features
        s.count += 1
        s.pc.setdefault(klass, 0)
        s.pc[klass] += 1
        for feat, val in features.items():
            s.pfc.setdefault(feat, dict())
            s.pfc[feat].setdefault(val, dict())
            s.pfc[feat][val].setdefault(klass, 0)
            s.pfc[feat][val][klass] += 1
            s.pf.setdefault(feat, dict())
            s.pf[feat].setdefault(val, 0)
            s.pf[feat][val] += 1

    def getpfc(s, feature, value, klass):
        "Helper function to index feature + value => class."
        assert feature and value and klass
        return s.pfc.get(feature, {}).get(value, {}).get(klass, None)

    def feature_values(s, feature):
        "Returns all observed values for feature."
        return sorted(s.pf.get(feature, {}).keys())

    def __most_common_class(s):
        i = sorted(s.pc.items(), lambda a,b: cmp(b[0], a[0]))
        return i[0][0]

    classes = property(lambda s: sorted(s.pc.keys()))
    features = property(lambda s: sorted(s.pf.keys()))
    most_common_class = property(lambda s: s.__most_common_class())

