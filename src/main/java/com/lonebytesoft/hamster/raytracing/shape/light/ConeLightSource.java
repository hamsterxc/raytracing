package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ConeLightSource<T extends Coordinates> extends PointLightSource<T> {

    private final T source;
    private final T direction;
    private final double cos;
    private final GeometryCalculator<T> geometryCalculator;

    public ConeLightSource(
            final T source,
            final T direction,
            final double angle,
            final double brightness,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(source, brightness, geometryCalculator);
        this.source = source;
        this.direction = direction;
        this.geometryCalculator = geometryCalculator;

        this.cos = Math.cos(Math.min(Math.abs(angle), Math.PI));
    }

    // todo: refactor this
    /*
        Calculating what ray parts are inside light cone
     */
    @Override
    protected Collection<LitAreaDefinition> calculateLitArea(Ray<T> ray) {
        final int dimensions = source.getDimensions();

        double p1 = 0;
        double p2 = 0;
        double p3 = 0;
        double p4 = 0;
        double p5 = 0;
        double p6 = 0;
        for(int i = 0; i < dimensions; i++) {
            final double d = ray.getDirection().getCoordinate(i);
            final double v = direction.getCoordinate(i);
            final double co = ray.getStart().getCoordinate(i) - source.getCoordinate(i);

            p1 += d * v;
            p2 += d * d;
            p3 += v * v;
            p4 += co * v;
            p5 += d * co;
            p6 += co * co;
        }

        final double cosSquare = cos * cos;
        final double a = p1 * p1 - cosSquare * p2 * p3;
        final double b = 2.0 * (p1 * p4 - p3 * p5 * cosSquare);
        final double c = p4 * p4 - p3 * p6 * cosSquare;
        final double d = b * b - 4.0 * a * c;

        final double threshold = MathCalculator.isEqual(p1, 0) ? 0 : -p4 / p1;

        if(cos >= 0) {
            if(MathCalculator.isEqual(p1, 0) && (p4 < 0)) {
                return Collections.emptyList();
            }

            if(d < 0) {
                if(a >= 0) {
                    // all values
                    if(p1 > 0) {
                        return Collections.singletonList(new LitAreaDefinition(threshold, Double.MAX_VALUE));
                    } else if(p1 < 0) {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, threshold));
                    } else {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                    }
                } else {
                    // no values
                    return Collections.emptyList();
                }
            } else {
                final LitAreaDefinition solution = calculateSolution(a, b, d);
                final double t1 = solution.getStart();
                final double t2 = solution.getEnd();

                if(a >= 0) {
                    // (-inf..t1] U [t2..+inf)
                    if(p1 > 0) {
                        if(threshold <= t1) {
                            return Arrays.asList(
                                    new LitAreaDefinition(threshold, t1),
                                    new LitAreaDefinition(t2, Double.MAX_VALUE)
                            );
                        } else {
                            return Collections.singletonList(new LitAreaDefinition(Math.max(threshold, t2), Double.MAX_VALUE));
                        }
                    } else if(p1 < 0) {
                        if(threshold >= t2) {
                            return Arrays.asList(
                                    new LitAreaDefinition(-Double.MAX_VALUE, t1),
                                    new LitAreaDefinition(t2, threshold)
                            );
                        } else {
                            return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Math.min(threshold, t1)));
                        }
                    } else {
                        return Arrays.asList(
                                new LitAreaDefinition(-Double.MAX_VALUE, t1),
                                new LitAreaDefinition(t2, Double.MAX_VALUE)
                        );
                    }
                } else {
                    // [t1..t2]
                    if(p1 > 0) {
                        if(threshold <= t2) {
                            return Collections.singletonList(new LitAreaDefinition(Math.max(threshold, t1), t2));
                        } else {
                            return Collections.emptyList();
                        }
                    } else if(p1 < 0) {
                        if(threshold >= t1) {
                            return Collections.singletonList(new LitAreaDefinition(t1, Math.min(threshold, t2)));
                        } else {
                            return Collections.emptyList();
                        }
                    } else {
                        return Collections.singletonList(new LitAreaDefinition(t1, t2));
                    }
                }
            }
        } else {
            if(d < 0) {
                if(a >= 0) {
                    if(p1 > 0) {
                        return Collections.singletonList(new LitAreaDefinition(threshold, Double.MAX_VALUE));
                    } else if(p1 < 0) {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, threshold));
                    } else {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                    }
                } else {
                    return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                }
            } else {
                final LitAreaDefinition solution = calculateSolution(a, b, d);
                final double t1 = solution.getStart();
                final double t2 = solution.getEnd();

                if(a >= 0) {
                    // [t1..t2]
                    if(p1 > 0) {
                        if(threshold <= t2) {
                            return Collections.singletonList(new LitAreaDefinition(Math.min(threshold, t1), Double.MAX_VALUE));
                        } else {
                            return Arrays.asList(
                                    new LitAreaDefinition(t1, t2),
                                    new LitAreaDefinition(threshold, Double.MAX_VALUE)
                            );
                        }
                    } else if(p1 < 0) {
                        if(threshold < t1) {
                            return Arrays.asList(
                                    new LitAreaDefinition(-Double.MAX_VALUE, threshold),
                                    new LitAreaDefinition(t1, t2)
                            );
                        } else {
                            return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Math.max(threshold, t2)));
                        }
                    } else {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                    }
                } else {
                    // (-inf..t1] U [t2..+inf)
                    if(p1 > 0) {
                        if(threshold <= t1) {
                            return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                        } else {
                            return Arrays.asList(
                                    new LitAreaDefinition(-Double.MAX_VALUE, t1),
                                    new LitAreaDefinition(Math.min(threshold, t2), Double.MAX_VALUE)
                            );
                        }
                    } else if(p1 < 0) {
                        if(threshold >= t2) {
                            return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                        } else {
                            return Arrays.asList(
                                    new LitAreaDefinition(-Double.MAX_VALUE, Math.max(threshold, t1)),
                                    new LitAreaDefinition(t2, Double.MAX_VALUE)
                            );
                        }
                    } else {
                        return Collections.singletonList(new LitAreaDefinition(-Double.MAX_VALUE, Double.MAX_VALUE));
                    }
                }
            }
        }
    }

    private LitAreaDefinition calculateSolution(final double a, final double b, final double d) {
        final double dsqrt = Math.sqrt(d);
        final double t1 = (-b - dsqrt) / (2.0 * a);
        final double t2 = (-b + dsqrt) / (2.0 * a);

        if(t1 <= t2) {
            return new LitAreaDefinition(t1, t2);
        } else {
            return new LitAreaDefinition(t2, t1);
        }
    }

    @Override
    protected Double calculateCollisionDistance(T point) {
        final T vector = geometryCalculator.subtract(point, source);
        final double cos = geometryCalculator.product(vector, direction) /
                (geometryCalculator.length(vector) * geometryCalculator.length(direction));
        if(cos >= this.cos) {
            return super.calculateCollisionDistance(point);
        } else {
            return 0.0;
        }
    }

}
