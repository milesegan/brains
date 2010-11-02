import unittest

import brains

class DecisionTreeTestCase(unittest.TestCase):

    def test_classify(s):
        f = brains.DataFile("data/mushroom.csv")
        test, train = f.split(4)
        d = brains.DecisionTree(f.features, train)
        #s.assertEqual(d.count, 8192)
