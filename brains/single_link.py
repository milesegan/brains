class SingleLink(object):
    "Implements single-link clustering method."
    def __init__(s):
        pass

    def cluster(s, k, points):
        "Returns points in k clusters, as a list."
        assert k > 0 and points
        distance = 0.1
        while True:
            # loop with increasing distance until 
            # cluster size is correct
            distance += 0.1 # TODO: this may need tuning/parameterization
            clusters = []
            allp = points
            while allp:
                # build clusters at this distance
                p, others = allp[0], allp[1:]
                close = []
                far = []
                for i in others:
                    if s.__distance(p, i) < distance:
                        close.append(i)
                    else:
                        far.append(i)
                close.append(p)
                clusters.append(close)
                allp = far
            if len(clusters) <= k:
                return clusters
            
    def __distance(s, a, b):
        "Calculates euclidean distance between a and b."
        # TODO: refactor this in common with kmeans
        assert a and b
        dist = 0
        for k in a.keys():
            dist += (a[k] - b[k]) ** 2
        return dist
