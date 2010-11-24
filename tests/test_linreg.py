import unittest

from brains import DataFile, LinearRegressor
import numpy as np

class TestLinearRegressor(unittest.TestCase):
    
    def test_train(s):
        d = np.loadtxt("data/iris.csv", skiprows = 6, delimiter = ",", comments = "#")
        # shuffle & normalize data
        np.random.shuffle(d)
        d /= np.abs(d).max(axis=0)
        d -= d.mean(axis = 0)
        targets, inputs = d[:,0], d[:,1:]
        targets.shape = (targets.shape[0], 1)
        split = d.shape[0] / 5
        testdata, traindata = inputs[:split,:], inputs[split:,:]
        testtargets, traintargets = targets[:split,:], targets[split:,:]
        n = LinearRegressor()
        n.train(traindata, traintargets)
        p = n.predict(testdata)
        for i in range(len(p)):
            print testtargets[i], p[i]
