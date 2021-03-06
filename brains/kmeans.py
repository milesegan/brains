import random

class KMeans(object):
    "Clusters data points by k-means method."

    def cluster(s, k, points):
        "Clusters by euclidean distance. Returns clustered points as a list."
        assert k > 0 and points is not None
        centroids = s._pick_initial_centroids(k, points)
        while True:
            newp = [list() for i in range(k)]
            for p in points:
                closest = s._closest_centroid(p, centroids)
                newp[closest].append(p)
            newc = [s._centroid(newp[i]) or centroids[i] for i in range(k)]
            if newc == centroids:
                return newp
            centroids = newc

    def _closest_centroid(s, p, centroids):
        assert p and centroids
        distances = [s._distance(p, c) for c in centroids]
        closest = sorted(range(len(distances)), lambda a, b: int(distances[a] - distances[b]))
        return closest[0]

    def _pick_initial_centroids(s, k, points):
        assert k > 0 and points
        random.shuffle(points)
        return points[:k]

    def _distance(s, a, b):
        assert a and b
        dist = 0
        for k in a.keys():
            dist += (a[k] - b[k]) ** 2
        return dist

    def _centroid(s, points):
        if not points:
            return None
        sums = {}
        for k in points[0].keys():
            for p in points:
                sums.setdefault(k, 0)
                sums[k] += p[k]
            sums[k] = sums[k] / len(points)
        return sums
