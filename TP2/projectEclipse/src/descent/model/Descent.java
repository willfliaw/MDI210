package descent.model;

import java.util.Observable;

import descent.model.Couple;

import problem.Pb;

/**
 * Main class that contains the method of descent
 */
public class Descent extends Observable implements Runnable {

  private double threshold = 0.00001; // if the gradient has a length below the threshold, the descent method stops
  private Pb pb; // the problem treated
  private Couple P = null; // must contain the current point of the plane in the descent method
  private Domain domain; // the domain in which we look for the minimum of the function
  private int nbSteps = 0; // is used to count the number of steps of the descent method
  private Couple direction; // for the direction that the descent method is going to follow
  private boolean suspend; // serves to suspend the descent
  private boolean finished; // is wrong while descending
  private boolean stopped; // goes to true if the descent method is interrupted by the user
  private boolean reachesdMinimum = true; // goes to false if the descent method shows that the problem is not bounded
  private int delay = 2000; /*
                             * serves to slow the descent for graphical display;
                             * delay in milliseconds between two steps of the descent
                             */
  public static double epsilon = 1E-12;

  /**
   * A double is considered null if its absolute value is lower than epsilon
   *
   * @param v the considered double
   * @return true if the number is considered null, false otherwise
   */
  public static boolean isNull(double v) {
    return v < epsilon && v > -epsilon;
  }

  /**
   * @param pb the considered problem ; the problem is always to search the
   *           minimum
   *           of a convex function of two variables on a domain of the plan
   *           limited by lines.
   */
  public Descent(Pb pb) {
    this.pb = pb;
    this.domain = pb.getDomaine();
  }

  /**
   * Do three things:
   * <br>
   * - notify observers, here the graphic interface, so that it updates the
   * display
   * <br>
   * - suspends the running thread for a minimum number of milliseconds equal to
   * the delay value
   * <br>
   * - if the method is suspended, wait until receiving a notification when the
   * method is no longer
   * suspended.
   */
  public synchronized void preventAndWait() {
    this.setChanged();
    this.notifyObservers();
    try {
      this.wait(delay);
      while (suspend)
        wait();
    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  /**
   * The descent method is started in a thread running this run method.
   */
  public void run() {
    if (P != null)
      perform();
  }

  /**
   * Performs the descent from point P.
   */
  public void perform() {
    // a passage in the loop for each step of the descent method
    do {
      this.nextDirection();
      this.preventAndWait();
      if (!this.finished) {
        this.P = this.nextP();
        if (this.P == null) { // la fonction f n'atteint pas de minimum sur le domaine
          this.reachesdMinimum = false;
          this.finished = true;
        }
      }
      nbSteps++;
    } while (!this.finished && !this.stopped);
    this.preventAndWait();
  }

  /**
   * METHOD TO IMPLEMENT
   * Search the next direction to follow when the current point P
   * is in the strict interior of the domain.
   * If the descent is not finished, put the next direction in the attribute
   * this.direction
   * (it is mandatory to use this attribute).
   * <br>
   * WARNING: if the descent method is finished, the method must set to true the
   * attribute this.finished.
   *
   * @see Couple#norm()
   * @see Couple#mult(double)
   * @see Pb#gradientf(Couple)
   * @see #threshold
   */
  private void directionToFollowIfPInside() {
    Couple gradient = this.pb.gradientf(this.P);

    if (gradient.norm() > this.threshold)
      this.direction = gradient.mult(-1.0 / gradient.norm());
    else
      this.finished = true;
  }

  /**
   * METHOD TO IMPLEMENT
   * Search the next direction to follow when the current point P
   * is on an edge of the domain but not in a corner.
   * If the descent is not finished, put the next direction in the attribute
   * this.direction
   * (it is mandatory to use this attribute).
   * <br>
   * According to the direction of the gradient of f and if the descent is not
   * completed, the next direction
   * may be towards the strict interior of the domain or along the edge in one
   * direction or the other.
   * <br>
   * WARNING: if the descent method is finished, the method must set to true the
   * attribute this.finished.
   *
   * @see Couple#norm()
   * @see Couple#scalarProduct(Couple)
   * @see Couple#isPerpendicular(Couple)
   * @see Couple#mult(double)
   * @see Constraint#getGradient()
   * @see Constraint#getUnitaryEdgeVector()
   * @see Pb#gradientf(Couple)
   * @see #threshold *
   * @param c the constraint saturated by the current point P
   */
  private void directionToFollowIfPOnEdge(Constraint c) {
    Couple gradient = this.pb.gradientf(this.P);

    if (gradient.norm() > threshold) {
      if (gradient.scalarProduct(c.getGradient()) > 0.0)
        this.direction = gradient.mult(-1.0 / gradient.norm());
      else {
        Couple d = c.getUnitaryEdgeVector();

        if (gradient.isPerpendicular(d))
          this.finished = true;
        else {
          if (gradient.scalarProduct(d) > 0.0)
            this.direction = d.mult(-1.0);
          else
            this.direction = d;
        }
      }
    } else
      this.finished = true;
  }

  /**
   * METHOD TO IMPLEMENT
   * Search the next direction to follow when the current point P
   * is in a corner of the domain.
   * If the descent is not finished, put the next direction in the attribute
   * this.direction
   * (it is mandatory to use this attribute).
   * <br>
   * WARNING: if the descent method is finished, the method must set to true the
   * attribute this.finished.
   * <br>
   * See the subject on
   * http://www.infres.enst.fr/~hudry/optim/descentEnglish/index.html for
   * explanations.
   * The method decompose being static, it is called with Couple.decompose...
   *
   * @see Couple#norm()
   * @see Couple#scalarProduct(Couple)
   * @see Couple#mult(double)
   * @see Couple#decompose(Couple, Couple, Couple)
   * @see Constraint#getGradient()
   * @see Constraint#getUnitaryEdgeVector()
   * @see Pb#gradientf(Couple)
   * @param corner an array with two boxes for the two constraints saturated by
   *               the current point P
   */
  private void directionToFollowIfPOnCorner(Constraint[] corner) {
    Couple gradient = pb.gradientf(this.P);
    Constraint c1 = corner[0], c2 = corner[1];
    Couple u1 = c1.getUnitaryEdgeVector(), u2 = c2.getUnitaryEdgeVector();

    if (u1.scalarProduct(c2.getGradient()) > 0.0)
      u1 = u1.mult(-1.0);
    if (u2.scalarProduct(c1.getGradient()) > 0.0)
      u2 = u2.mult(-1.0);

    Couple n1 = c1.getGradient().mult(-1.0), n2 = c2.getGradient().mult(-1.0);
    double p1 = u1.scalarProduct(gradient), p2 = u2.scalarProduct(gradient);

    if (gradient.norm() > threshold) {
      if (n1.scalarProduct(gradient) <= 0.0 && n2.scalarProduct(gradient) <= 0.0)
        this.direction = gradient.mult(-1.0 / gradient.norm());
      else if (p1 >= 0.0 && p2 >= 0.0)
        this.finished = true;
      else {
        if (p1 <= p2)
          this.direction = u1;
        else
          this.direction = u2;
      }
    } else
      this.finished = true;
  }

  /**
   * METHOD TO IMPLEMENT
   * We consider a half-line parameterized by t -> startingPoint + t.dir (t >= 0);
   * we set phi(t) = f(startingPoint + t.dir); we suppose that we have phi'(0) <
   * 0;
   * we look for a point P = startingPoint + t.dir, t > 0, with phi'(t) > 0.
   * Recall that, by hypothesis, the function f is convex, which means that phi'
   * is increasing.
   * You can first test t = 1 and then, if necessary, double the value of t
   * successively.
   * The derivative of t -> f(startingPoint + t.dir) can be written :
   * pb.phiDerivative(startingPoint, dir, t);
   * see Pb#phiDerivative (Couple, Couple, double)
   *
   * @param startingPoint the origin of the half-line.
   * @param dir           the direction of the half-line.
   * @return if you do not find such a point with t < Double.MAX_VALUE/2, the
   *         method returns -1;
   *         otherwise the method returns a value of t with phi'(t) > 0.
   */
  public double searchSecondPoint(Couple startingPoint, Couple dir) {
    double t = 1.0;
    for (double derivative = this.pb.phiDerivative(startingPoint, dir, t); derivative <= 0.0; derivative = this.pb
        .phiDerivative(startingPoint, dir, t)) {
      if (t >= Double.MAX_VALUE / 2)
        return -1.0;

      t *= 2.0;
    }

    return t;
  }

  /**
   * METHOD TO IMPLEMENT
   * We consider a half-line parameterized by t -> startingPoint + t.dir (t> = 0);
   * We set phi(t) = f (startingPoint + t.dir); we suppose that we have phi'(0) <
   * 0
   * and phi'(t1) > 0;
   * We look for a point P = startingPoint + t * dir between startingPoint and
   * startingPoint + t1 * dir with phi'(t) = 0.
   * For this, we proceed by dichotomy.
   * One can use the static method isNull of this class to test if a double value
   * is null or not.
   * The derivative of t -> f(startingPoint + t.dir) can be written :
   * pb.phiDerivative(startingPoint, dir, t);
   *
   * @see Pb#phiDerivative (Couple, Couple, double)
   * @see Descent#isNull
   * @see Couple#add(Couple)
   * @param startingPoint the origin of the half-line.
   * @param dir           the direction of the half-line.
   * @param t1            parameter such that phi'(t1)> 0.
   * @return the point P = startPoint + t * dir such that phi'(t) = 0 (or nearly).
   */
  public Couple dichotomy(Couple startingPoint, Couple dir, double t1) {
    double tp0 = 0, tp1 = t1;

    double derivative = pb.phiDerivative(startingPoint, dir, (tp0 + tp1) / 2.0);
    do {
      derivative = pb.phiDerivative(startingPoint, dir, (tp0 + tp1) / 2.0);

      if (derivative > 0.0)
        tp1 = (tp0 + tp1) / 2;
      else if (derivative < 0)
        tp0 = (tp0 + tp1) / 2;
    } while (!isNull(derivative));

    return startingPoint.add(dir.mult((tp0 + tp1) / 2));
  }

  /**
   * METHOD TO IMPLEMENT
   * Check that this is a minimum using the condition of Karush, Kuhn and Tucker
   *
   * @param P The point where we check that it is a minimum..
   * @see Domain#isCorner(Constraint, Constraint)
   * @see Domain#isOnEdge(Couple)
   * @see Couple#norm()
   * @see Couple#scalarProduct(Couple)
   * @see Couple#isPerpendicular(Couple)
   * @see Couple#decompose(Couple, Couple, Couple)
   * @see Constraint#getUnitaryEdgeVector()
   * @see Constraint#getGradient()
   * @see Pb#gradientf(Couple)
   * @return null if the condition of Karush, Kuhn et Tucker is not satisfied
   *         <br>
   *         otherwise
   *         <br>
   *         - the couple of Lagrange multipliers if P is in a corner
   *         <br>
   *         - the couple forms by the multiplier of Lagrange and 0 if P is on an
   *         edge
   *         <br>
   *         - (0, 0) if P is inside
   *
   */
  public Couple KarushKuhnTucker(Couple P) {
    Constraint[] coin = this.domain.isCorner(this.P);
    Couple lambda = null, gradient = this.pb.gradientf(this.P);

    if (coin != null) {
      Couple decomposedGradient = Couple.decompose(gradient, coin[0].getGradient(), coin[1].getGradient());
      if (decomposedGradient.x < 0 && decomposedGradient.y < 0)
        lambda = decomposedGradient;
    } else if (this.domain.isOnEdge(P) != null) {
      Constraint c = this.domain.isOnEdge(P);

      if (gradient.isPerpendicular(c.getUnitaryEdgeVector())) {
        lambda = new Couple(-gradient.norm() / c.getGradient().norm(), 0);
      }
    } else if (isNull(gradient.norm()))
      lambda = new Couple(0, 0);

    return lambda;
  }

  /**
   * Look for the next direction to follow.
   * The method considers the cases where:
   * P is inside the domain,
   * P is on an edge,
   * P is on a corner
   * <br>
   * If the descent is finished, the method sets this.finished to true.
   * </font>
   */
  public void nextDirection() {
    Constraint c;
    Constraint[] corner;
    c = this.domain.isOnEdge(P);
    if (c != null) {
      corner = this.domain.isCorner(P);
      if (corner == null) {
        this.directionToFollowIfPOnEdge(c);
      } else {
        this.directionToFollowIfPOnCorner(corner);
      }
    } else {
      this.directionToFollowIfPInside();
    }
  }

  /**
   * Knowing the current point and the direction to follow, the method searches
   * for the next current point.
   *
   * @return
   *         this.P is the current point (which can be inside domain, on a
   *         edge of the domain or on a corner) and this.direction is the
   *         direction to follow; the method returns
   *         <br>
   *         - the next current point (which can be inside the domain, on an edge,
   *         on a corner)
   *         <br>
   *         - null if it showed that the function f does not reach a minimum
   *         on the considered domain.
   */
  public Couple nextP() {
    double t1;
    /*
     * Explanations for the following instruction.
     * We consider a half-line parameterezd by t -> this.P + t * this.direction;
     * this half-line can start from inside the domain, or from an edge, or from a
     * corner
     * and may skirt an edge of the domain.
     * If the half-line is whole in the domain which is only possible
     * if the domain is not bounded, the statement returns -1.
     * Otherwise, it returns a value of t > 0 for which this.P + t * this.direction
     * belongs
     * to an edge of the domain.
     */
    t1 = domain.intersection(this.P, this.direction);
    if (t1 == 0)
      return this.P;
    // If the half-line encounters an edge of the domain and if the derivative
    // of t -> this.P + t * this.direction is positive at this point of
    // intersection,
    // we return this point.

    if ((t1 > 0) && (pb.phiDerivative(this.P, this.direction, t1) <= 0))
      return this.P.add(this.direction.mult(t1));

    // If the half-line is whole in the domain, we look for a point where the
    // derivative of
    // t -> this.P + t * this.direction is positive
    if (t1 < 0) {
      t1 = searchSecondPoint(P, direction);
      // In the case below, the problem does not reach a minimum
      if (t1 < 0)
        return null;
      // otherwise the derivative of t -> this.P + t * this.direction is positive for
      // t = t1
    }
    return dichotomy(P, direction, t1);
  }

  /**
   * @return the problem deals
   */
  public Pb getPb() {
    return this.pb;
  }

  /**
   * @return the current point this.P
   */
  public Couple getP() {
    return this.P;
  }

  /**
   * @return the direction to follow this.direction.
   */
  public Couple getDirectionToFollow() {
    return this.direction;
  }

  /**
   * To initialize the starting point of the descent method
   *
   * @param P return the current point.
   */
  public void setP(Couple P) {
    this.P = P;
  }

  /**
   * permits to pause or continue the descent method.
   *
   * @param suspend if the parameter is true, the method is suspended, it is
   *                pursued
   *                if the parameter is false.
   */
  public void setSuspend(boolean suspend) {
    this.suspend = suspend;
  }

  /**
   * To know if the method of descent has been stopped.
   *
   * @return la valeur de l'attribut booleen stopped
   */
  public boolean isStopped() {
    return this.stopped;
  }

  /**
   * Used to stop the descent method
   */
  public void stop() {
    this.stopped = true;
    this.finished = true;
  }

  /**
   * @return true si la descente est terminee et false sinon
   */
  public boolean isFinished() {
    return this.finished;
  }

  /**
   * After the descent is completed, it permits to know if the problem has
   * reached its minimum or not.
   *
   * @return true or false depending on whether the problem is treating reaches
   *         its minimum or not
   */
  public boolean reachesItsMinimum() {
    return this.reachesdMinimum;
  }

  /**
   * To specify the threshold of the norm of the gradient.
   *
   * @param threshold if the gradient norm is below threshold, the descent method
   *                  stops
   */
  public void setThreshold(double threshold) {
    this.threshold = threshold;
  }

  /**
   * @return the number of steps taken since the beginning of the descent method
   */
  public int getNbSteps() {
    return this.nbSteps;
  }

  /**
   * @return waiting time between two steps of the descent method, in milliseconds
   */
  public int getDelay() {
    return delay;
  }

  /**
   * @param delay waiting time between two steps of the descent method, in
   *              milliseconds
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }
}
