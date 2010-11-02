# simple bayesian classifier in python

from __future__ import division
import random
from brains import ProbabilityMap

class Bayes(object):

    def __init__(s, features):
        s.pmap = ProbabilityMap()

    def train(s, klass, features):
        s.pmap.update(klass, features)

    def classify(s, features):
        ranked = []
        for klass in s.pmap.pc.iterkeys():
            p = 1
            for feat, val in features.items():
                c = s.pmap.getpfc(feat, val, klass)
                if c:
                    p *= c / s.pmap.count
                else:
                    p *= 0.01 / s.pmap.count # TODO: adjust this
            ranked.append((p * s.pmap.pc.get(klass, 0.0), klass))
        ranked.sort()
        return ranked[-1][1]
