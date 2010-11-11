import unittest

import brains

class DataFileTestCase(unittest.TestCase):

    def test_load(s):
        f = brains.DataFile("data/mushroom.csv")
        allp = [p for p in f.points()]
        s.assertEqual(f.count, 8124)
        s.assertEqual(f.features[0], "cap-shape")
        s.assertEqual(allp[0][0], "p")
        s.assertEqual(allp[0][1]["cap-shape"], "x")
