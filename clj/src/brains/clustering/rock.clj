(ns brains.clustering.rock
  (require [clojure.string :as string]))

public Dendrogram cluster() {

                             //  Create a new cluster out of every point.
                             List<Cluster> initialClusters = new ArrayList<Cluster>();
                             for(int i = 0, n = points.length; i < n; i++) {
                                     Cluster cluster = new Cluster(points[i]);
                                     initialClusters.add(cluster);
                                     }
                             double g = Double.POSITIVE_INFINITY;
                             Dendrogram dnd = new Dendrogram("Goodness");
                             dnd.addLevel(String.valueOf(g), initialClusters);

                             MergeGoodnessMeasure goodnessMeasure = new MergeGoodnessMeasure(th);

                             ROCKClusters allClusters = new ROCKClusters(initialClusters,
                                                                         linkMatrix,
                                                                         goodnessMeasure);

                             int nClusters = allClusters.size();
                             while( nClusters > k ) {
                                                     int nClustersBeforeMerge = nClusters;
                                                     g = allClusters.mergeBestCandidates();
                                                     nClusters = allClusters.size();
                                                     if( nClusters == nClustersBeforeMerge ) {
                                                                                              // there are no linked clusters to merge
                                                                                              break;
                                                                                              }
                                                     dnd.addLevel(String.valueOf(g), allClusters.getAllClusters());
                                                     }

                             System.out.println("Number of clusters: "+allClusters.getAllClusters().size());
                             return dnd;
                             }
