from __future__ import division

import unittest
import brains

class DecisionTreeTestCase(unittest.TestCase):

    def test_classify(s):
        f = brains.DataFile("data/mushroom.csv")
        test, train = f.split(4)
        d = brains.DecisionTree(f.features, train)
        correct = 0
        for t in test:
            c = d.classify(t[1])
            if c == t[0]:
                correct += 1
        assert correct / len(test) > 0.9
