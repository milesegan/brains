import random
import unittest

from brains import DataFile, SingleLink

class TestSingleLink(unittest.TestCase):
    def test_cluster(s):
        d = DataFile("data/iris.csv", float)
        k = SingleLink()
        points = [p[1] for p in d.points()]
        random.shuffle(points)
        clust = k.cluster(3, points)
        lengths = [len(i) for i in clust]
        s.assertEqual(sum(lengths), d.count)
        s.assertEqual(len(clust), 3)
