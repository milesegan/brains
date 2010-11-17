import random
import unittest

from brains import DataFile, SpanningTree

class TestSpanningTree(unittest.TestCase):
    def test_cluster(s):
        d = DataFile("data/iris.csv", float)
        k = SpanningTree()
        points = [p[1] for p in d.points()]
        random.shuffle(points)
        clust = k.cluster(3, points)
        lengths = [len(i) for i in clust]
        assert sum(lengths) == d.count
        assert len(clust) == 3
