from __future__ import division
import random
from brains import ProbabilityMap

class Bayes(object):
    "Naive bayes classifier."
    def __init__(s):
        s.pmap = ProbabilityMap()

    def train(s, klass, features):
        "Adds the sample with given class and features."
        s.pmap.update(klass, features)

    @property
    def count(s):
        "Returns the number of samples trained by this classifier."
        return s.pmap.count

    def classify(s, features):
        "Classifies the sample with features. Returns most probable class"
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
