class SpanningTree(object):
    "Implements spanning-tree clustering method."
    "See http://en.wikipedia.org/wiki/Kirchhoff's_theorem ."
    def __init__(s):
        s.cache = {}

    def cluster(s, k, points):
        "Clusters points into k clusters."
        assert k and points
        distance = 0.1
        while True:
            # iteratively increase distance tolerance until
            # k clusters form
            distance += 0.1 # TODO: tune or parameterize this
            clusters, allp = [[points[0]]], points[1:]
            while allp:
                # find closest point
                head = clusters[-1][-1]
                allp.sort(key = lambda p: s.__distance(p, head))
                if s.__distance(head, allp[0]) < distance:
                    # within distance, extend tree
                    clusters[-1].append(allp[0])
                else:
                    # too far, start new tree
                    clusters.append([allp[0]])
                allp = allp[1:]
            if len(clusters) <= k:
                return clusters

    def __distance(s, a, b):
        "Calculates euclidean distance between a and b."
        # TODO: refactor this in common with kmeans
        dist = 0
        for k in a.keys():
            dist += (a[k] - b[k]) ** 2
        return dist
