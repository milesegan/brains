from __future__ import division
import math
import numpy as np

class NeuralNet(object):
    """Basic single-layer neural net implementation."""
    def __init__(s, n_inputs, n_outputs, n_hidden):
        assert n_inputs > 0 and n_outputs > 0 and n_hidden > 0
        s.eta = 0.1 # learning rate
        s.n_inputs = n_inputs
        s.n_outputs = n_outputs
        s.n_hidden = n_hidden
        # initialize weights to small random number
        s.iweights = (np.random.rand(n_inputs, n_hidden) - 0.5) * 2 / math.sqrt(n_inputs)
        s.oweights = (np.random.rand(n_hidden, n_outputs) - 0.5) * 2 / math.sqrt(n_hidden)

    def train(s, inputs, outputs):
        """Trains the network with the given sample."""
        assert inputs.shape[1] == s.n_inputs
        assert outputs.shape[1] == s.n_outputs
        iterations = 0
        order = range(inputs.shape[0])
        while iterations < 2000:
            # find activations and error
            hact, oact = s.process(inputs)
            error = 0.5 * ((outputs - oact) ** 2).sum() / len(inputs)
            if error < 0.005: break
            oerr = (outputs - oact) * oact * (1.0 - oact)
            herr = hact * (1 - hact) * oerr.dot(s.oweights.T)

            # update weights
            updatei = s.eta * inputs.T.dot(herr)
            updateo = s.eta * hact.T.dot(oerr)
            s.oweights += updateo
            s.iweights += updatei

            # shuffle order of weights & inputs to speed learning
            np.random.shuffle(order)
            inputs = inputs[order,:]
            outputs = outputs[order,:]
            iterations += 1

    def process(s, inputs):
        """Runs the neural net on the given inputs."""
        assert inputs.shape[1] == s.n_inputs
        hact = inputs.dot(s.iweights)
        hact = 1 / (1 + np.exp(-hact))
        oact = hact.dot(s.oweights)
        oact = 1 / (1 + np.exp(-oact))
        return hact, oact
    
