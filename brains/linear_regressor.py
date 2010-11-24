import numpy as np

class LinearRegressor(object):
    "Performs standard linear regression on a dataset."

    def train(s, inputs, targets):
        s.beta = np.linalg.inv(inputs.T.dot(inputs))
        s.beta = s.beta.dot(inputs.T)
        s.beta = s.beta.dot(targets)

    def predict(s, inputs):
        return inputs.dot(s.beta)
