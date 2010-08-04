import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.System.out;

class RatingsMap extends HashMap<Integer,Map<Integer,Double>> {}

class MovieLens {
    
    private RatingsMap userRatings;
    private RatingsMap movieRatings;
    private Map<Integer,String> movies;
    final static String SEPARATOR = "\\|";

    public MovieLens(BufferedReader movieFile, BufferedReader ratingFile) throws IOException {
        userRatings = new RatingsMap();
        movieRatings = new RatingsMap();
        movies = new HashMap<Integer,String>();

        String line = null;
        while ((line = movieFile.readLine()) != null) {
            String[] parts = line.split(SEPARATOR);
            movies.put(new Integer(parts[0]), parts[1]);
        }

        while ((line = ratingFile.readLine()) != null) {
            String[] parts = line.split("\\s");
            int user = new Integer(parts[0]);
            int movie = new Integer(parts[1]);
            double rating = new Double(parts[2]);
            if (!movieRatings.containsKey(movie)) {
                movieRatings.put(movie, new HashMap<Integer,Double>());
            }
            movieRatings.get(movie).put(user, rating);
            if (!userRatings.containsKey(user)) {
                userRatings.put(user, new HashMap<Integer,Double>());
            }
            userRatings.get(user).put(movie, rating);
        }
    }

    public Map<Integer,String> getMovies() {
        return movies;
    }

    public Double similarity(int a, int b) {
        if (!movieRatings.containsKey(a) && !movieRatings.containsKey(b)) return 0.0;
        Map<Integer,Double> ratingsA = movieRatings.get(a);
        Map<Integer,Double> ratingsB = movieRatings.get(b);

        double meanAllA = mean(ratingsA.values().toArray(new Double[1]));
        double meanAllB = mean(ratingsB.values().toArray(new Double[1]));
        Vector<Double> commonA = new Vector<Double>();
        Vector<Double> commonB = new Vector<Double>();

        // find ratings of other movies by same user
        // subtract mean and store deltas
        for (Map.Entry<Integer,Double> kv : ratingsA.entrySet()) {
            if (ratingsB.containsKey(kv.getKey())) {
                commonA.add(kv.getValue() - meanAllA);
                commonB.add(ratingsB.get(kv.getKey()) - meanAllB);
            }
        }
        if (commonA.size() < 3) return 0.0;

        return pearsonCorrelation(commonA.toArray(new Double[0]), commonB.toArray(new Double[0]));
    }

    protected static BufferedReader openFile(String path) throws Exception {
        return new BufferedReader(new FileReader(path));
    }

    public static void main(String[] args) throws Exception {
        MovieLens ml = new MovieLens(openFile(args[0]), openFile(args[1]));
        Set<Integer> movies = ml.getMovies().keySet();
        List<Object[]> sims = new ArrayList<Object[]>();
        for (Integer movie : movies) {
            double sim = ml.similarity(86, movie);
            sims.add(new Object[] { sim, ml.getMovies().get(movie) });
        }
        Collections.sort(sims, new Comparator<Object[]>() {
                public int compare(Object[] a, Object[] b) {
                    return (int)((Double)a[0] - (Double)b[0]);
                }
            });
        for (Object[] i : sims) {
            out.format("%3f %s\n", i[0], i[1]);
        }
    }

    public static double mean(Double[] values) {
        double sum = 0.0;
        for (Double d : values) {
            sum += d;
        }
        return sum / values.length;
    }

    public static Double standardDeviation(Double mean, Double[] values) {
        double squares = 0.0;
        for (Double v : values ) {
            squares += (v - mean) * (v - mean);
        }
        return Math.sqrt(squares / values.length);
    }

    public static Double pearsonCorrelation(Double[] a, Double[] b) {
        if (a.length == 0) return 0.0; // no overlapping ratings

        double xy = 0.0d;
        double meanA = mean(a);
        double devA = standardDeviation(meanA, a);
        double meanB = mean(b);
        double devB = standardDeviation(meanB, b);

        for (int i = 0; i < a.length; i++) {
            xy += (a[i] - meanA) * (b[i] - meanB);
        }
    
        if (devA == 0.0 || devB == 0.0) {
            double indA = 0.0;
            double indB = 0.0;
            for (int i = 1; i < a.length; i++) {
                indA += a[i - 1] - a[i];
                indB += b[i - 1] - b[i];
            }
            if (indA == 0.0 && indB == 0.0) {
            // degenerate correlation, all points the same
                return 1.0;
            }
            else if (devA == 0) {
                // otherwise either a or b vary
                devA = devB;
            }
            else {
                devB = devA;
            }
        }

        return xy / (a.length * devA * devB);
    }

}

/*
def main(args:Array[String]) = {
  val set = new MovieLens(
    openFile(args(0)),
    openFile(args(1)),
    openFile(args(2)))

  val movies = set.movies.keySet
  var sims = Seq[Tuple2[Double,String]]()
  movies.foreach { m =>
    val sim = set.similarity(86, m)
    sims = sims :+ (sim, set.movies(m))
  }
  sims.sorted.foreach { i =>
    //printf("%3f %s\n", i._1, i._2)
  }
*/
