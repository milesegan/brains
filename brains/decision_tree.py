from __future__ import division

import math
from brains import ProbabilityMap

class DecisionTree(object):

    def __init__(s, features, points):
        s.pm = ProbabilityMap(points)
        s.tree = {}
        s.__train(s.tree, s.pm, points)

    def __train(s, tree, pm, points):
        if len(pm.classes) == 1:
            return pm.classes[0]
        elif len(pm.features) == 0 or len(points) == 0:
            return s.pm.most_common_class

        # pm = ProbabilityMap(points)
        # igains = [(s.__info_gain(f, pm), f) for f in pm.features]
        # igains.sort()
        # igains.reverse()
        # best = igains[0][1]
        # rest = [f[1] for f in igains[1:]]
        # tree[best] = {}
        # tree = tree[best]
        # for feat in rest:
        #     for p in points:
                
        # features.remove(best)
        # print(igains)

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
        print(p)
        return -p * math.log(p)
