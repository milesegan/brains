import unittest

from brains import DataFile, KMeans

class TestKMeans(unittest.TestCase):

    def test_cluster(s):
        d = DataFile("data/iris.csv", float)
        k = KMeans()
        clust = k.cluster(3, [p[1] for p in d.points()])
        s.assertEqual(len(clust), 3)
