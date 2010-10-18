#!/usr/bin/env python

from __future__ import division

import numpy as np
import math, random, sys

class ProbabilityMap(object):
    def __init__(s, features, points):
        s.features = features
        s.count = len(points)
        s.pc = {} # class probability
        s.pf = {} # feature probability
        s.pfc = {} # feature-class probabiltiy
        for d in points:
            klass, feats = d[0], d[1]
            s.pc.setdefault(klass, 0)
            s.pc[klass] += 1
            feats = (x for x in zip(features, feats))
            for f in feats:
                s.pfc.setdefault(f[0], dict())
                s.pfc[f[0]].setdefault(f[1], dict())
                s.pfc[f[0]][f[1]].setdefault(klass, 0)
                s.pfc[f[0]][f[1]][klass] += 1
                s.pf.setdefault(f[0], dict())
                s.pf[f[0]].setdefault(f[1], 0)
                s.pf[f[0]][f[1]] += 1

class DecisionTree(object):
    def __init__(s, features, points):
        s.features = features
        s.points = points
        s.count = len(points)
        s.__train(features, points)

    def __train(s, features, points):
        pm = ProbabilityMap(features, points)
        igains = [(f, s.__info_gain(f, pm)) for f in features]
        print(igains)

    def __info_gain(s, feature, pm):
        class_e = [s.__entropy(pm.pc[c] / pm.count) for c in pm.pc.keys()]
        all_e = []
        for c in pm.pc.keys():
            for v, pv in pm.pf[feature].items():
                p = pm.pfc[feature].get(v, {}).get(c, 0)
                print(p, pv, pm.count)
                all_e.append(s.__entropy(p / pm.count) * pv / pm.count)

        return class_e.sum - all_e.sum

    def __entropy(s, p):
        print(p)
        return -p * math.log(p)
                
def main():
    data = np.genfromtxt(sys.argv[1], delimiter = ",", dtype = None)
    features = data[0][1:]
    points = [[i[0], i[1:]] for i in data[1:]]
    random.shuffle(points)
    cut = int(len(points) / 4)
    test = points[:cut]
    train = points[cut:]
    d = DecisionTree(features, points)

main()
