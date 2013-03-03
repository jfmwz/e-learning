import java.util.Arrays;

public class Fast {
    private static class Result {
        private final int start;
        private final int end;

        public Result(int s, int e) {
            start = s;
            end = e;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    private static Result binarySearchBySlope(Point[] sortedBySlope,
                                                Point reference,
                                                double slope) {
        int left = 0,
            right = sortedBySlope.length - 1,
            start = 0,
            end = 0;

        while (left <= right) {
            int middle = (left + right) / 2;
            double referenceSlope = sortedBySlope[middle].slopeTo(reference);

            if (referenceSlope == slope) {
                start = middle;
                end = middle;

                while (true) {
                    double less = Double.POSITIVE_INFINITY,
                            greater = Double.NEGATIVE_INFINITY;

                    if (start - 1 > 0) {
                        less = sortedBySlope[start - 1].slopeTo(reference);

                        if (less == slope) {
                            if (sortedBySlope[start - 1]
                                    .compareTo(sortedBySlope[start]) < 0) {
                                --start;
                            }
                        }
                    } else {
                        less = Double.POSITIVE_INFINITY;
                    }

                    if (end  + 1 < sortedBySlope.length) {
                        greater = sortedBySlope[end + 1].slopeTo(reference);

                        if (greater == slope) {
                            if (sortedBySlope[end]
                                    .compareTo(sortedBySlope[end + 1]) < 0) {
                                ++end;
                            }
                        }
                    } else {
                        greater = Double.NEGATIVE_INFINITY;
                    }

                    if (greater != slope && less != slope) {
                        break;
                    }
                }
            }

            if (referenceSlope < slope) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return new Result(start, end);
    }

    public static void main(String[] args) throws Exception {
        String filename = args[0];
        In in = new In(filename);

        int N = in.readInt();

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();

            points[i] = new Point(x, y);
            points[i].draw();
        }

        Arrays.sort(points);

        Point[][] slopes = new Point[N][N];

        for (int i = 0; i < N; ++i) {
            Point referencePoint = points[i];

            System.arraycopy(points, 0, slopes[i], 0, N);
            Arrays.sort(slopes[i], referencePoint.SLOPE_ORDER);
        }

        Result last = new Result(-1, -1);

        for (int i = 0; i < N - 2; ++i) {
            for (int j = i + 1; j < N - 1; ++j) {
                double slope = points[i].slopeTo(points[j]);

                Result indices = binarySearchBySlope(slopes[i], points[i], slope);

                if (indices.start != last.start && indices.end != last.end) {
                    if (indices.end - indices.start >= 2) {
                        int start = indices.getStart();
                        int end = indices.getEnd();

                        if (points[i].compareTo(slopes[i][start]) >= 0) {
                            continue;
                        }

                        StdOut.print(points[i]);

                        for (int k = start; k <= end; ++k) {
                            StdOut.print(" -> " + slopes[i][k]);
                        }

                        StdOut.println();

                        points[i].drawTo(slopes[i][end]);
                        last = indices;
                    }
                }
            }
        }

        StdDraw.show(0);
    }
}