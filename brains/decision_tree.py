from __future__ import division

import math
from brains import ProbabilityMap

class DecisionTree(object):

    def __init__(s, features, points):
        s.most_common_class = None
        s.tree = s.__train(points)

    def classify(s, features):
        tree = s.tree
        while True:
            v = features.get(tree[0], None)
            if v == None:
                return s.most_common_class
            n = tree[1].get(v, None)
            if n == None:
                return s.most_common_class
            elif type(n) == str:
                return n
            else:
                tree = n

    def __train(s, points):
        if len(points) == 0:
            return None

        pm = ProbabilityMap(points)
        if s.most_common_class == None:
            # save for later as default value
            s.most_common_class = pm.most_common_class
        if len(pm.classes) == 1:
            return pm.classes[0]
        if len(pm.features) == 0:
            return None

        # find highest entropy feature to split with
        igains = [(s.__info_gain(f, pm), f) for f in pm.features]
        igains.sort()
        igains.reverse()
        best = igains[0][1]
        rest = [f[1] for f in igains[1:]]
        tree = [best, {}]
        
        # split remaining data points on best feature
        for val in pm.feature_values(best):
            newp = []
            for p in points:
                if p[1].get(best, None) == val:
                    del p[1][best]
                    newp.append(p)
            tree[1][val] = s.__train(newp)
        return tree

    def __info_gain(s, feature, pm):
        class_e = [s.__entropy(pm.pc[c] / pm.count) for c in pm.pc.keys()]
        all_e = []
        for c in pm.pc.keys():
            for v, pv in pm.pf.get(feature, {}).items():
                p = pm.getpfc(feature, v, c)
                if p:
                    all_e.append(s.__entropy(p / pm.count) * pv / pm.count)
        return sum(class_e) - sum(all_e)

    def __entropy(s, p):
        return -p * math.log(p)
