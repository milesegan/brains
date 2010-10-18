import unittest

import brains

class DataFileTestCase(unittest.TestCase):

    def test_load(s):
        f = brains.DataFile("data/mushroom.csv")
        for p in f.points():
            pass
        s.assertEqual(f.count, 8124)
        s.assertEqual(f.features[0], "cap-shape")
