<!-- # Synthesis

Author: William Liaw

- La linéarisation en optimisation non linéaire avec contraintes (parties VIII.5.2. et VIII.5.3. du polycopié) est hors programme de l'examen de MDI210
- Toutes les autres parties du polycopié peuvent faire l'objet de questions dans l'examen
- Les documents autorisés lors de l'épreuve sont limités à deux feuilles recto verso (soit quatre pages) de format A4 de notes synthétiques personnelles, manuscrites ou dactylographiées, et un dictionnaire pour les élèves dont le français n'est pas la langue maternelle. -->

# Linear systems

- $Ax = b$, where $x \in \mathbb{R}^n$ is unknown, $A \in \mathbb{R}^{m \times n}$, and $b \in \mathbb{R}^m$ is given:
  $$
  A = (a_{ij}) =
  \begin{bmatrix}
  a_{11} & \dots  & a_{1n} \\
  \vdots & \ddots & \vdots \\
  a_{m3} & \dots  & a_{mn}
  \end{bmatrix}
  $$
- Normal matrices: $AA^T = A^T A$
  - Symmetric matrices: $(a_{ij}) = A = A^T = (a_{ji})$
    - The eigenvalues are always real
    - The eigenvectors corresponding to distinct eigenvalues are always orthogonal
  - Orthogonal matrices: $AA^T = A^T A = I$ where $I = (\delta_{ij})$ denotes the identity matrix
    - Product of orthogonal matrix is also orthogonal
- Inverse of a $2\times 2$ matrix:
  $$
  A =
  \begin{bmatrix}
  a & b \\
  c & d
  \end{bmatrix}
  \Rightarrow
  A^{-1} = \frac{1}{ad - bc}
  \begin{bmatrix}
  d  & -b \\
  -c & a
  \end{bmatrix}
  $$
- Norm: the function $||\cdot||:x\in\mathbb{R}^n\rightarrow\mathbb{R}_+$ is a norm if it is
  - Point separating: $||x|| = 0\Leftrightarrow x = 0,\ \forall x\in E$
  - Sub-additive: $||x + y|| \leq ||x|| + ||y||,\ \forall x, y\in E$
  - Homogeneous: $||ax|| = |a|\ ||x||,\ \forall x\in E,\ a\in\mathbb{R}$
- Examples:
  - L1 norm: $||x||_1 := \sum\limits_{i=1}^n |x_i|$
  - L2 norm: $||x||_2 := \sqrt{\sum\limits_{i=1}^n x_i^2}$
  - Lp norm: $||x||_p := \big(\sum\limits_{i=1}^n |x_i|^p\big)^{1/p}$
  - Infinity norm: $||x||_\infty := \max\limits_i |x_i|$
  - Unit sphere: $x \in V: ||x|| = 1$ for any vector space $V$
- *Induced* norms: $||A|| := \sup\limits_{x\in\mathbb{R}^n,\ x \neq 0}\frac{||Ax||}{||x||}$
  - All induced norms satisfy: $||Ax|| \leq ||A||\ ||x||,\ \forall x\in \mathbb{R}$ and are sub-multiplicative $(||AB|| \leq ||A||\ ||B||,\ \forall A, B\in \mathbb{R}^{m\times n})$
  - $||O||_2 = 1,\ \forall O\in\mathbb{R}^{n\times n}$ orthogonal matrix
- Other matrix norms and operators:
  - L1 norm: $||A||_1 := \sup\limits_{x\in\mathbb{R}^n,\ x \neq 0}\frac{||Ax||_1}{||x||_1}=\max\limits_{1\leq j\leq n}\sum\limits_{i=1}^n|a_{ij}|$
  - L2 norm: $||A||_2 := \sup\limits_{x\in\mathbb{R}^n,\ x \neq 0}\frac{||Ax||_2}{||x||_2}$
  - Infinity norm: $||A||_\infty := \sup\limits_{x\in\mathbb{R}^n,\ x \neq 0}\frac{||Ax||_\infty}{||x||_\infty}=\max\limits_{1\leq i\leq n}\sum\limits_{j=1}^n|a_{ij}|$
  - Trace: $\text{Tr}(A) := \sum\limits_{i=1}^n a_{ii}$
    - Notice that: $\text{Tr}(AB) = \text{Tr}(BA)$
      - Notice that: $\text{Tr}(P^{-1}AP) = \text{Tr}(A)$
  - Frobenius norm: $||A||_F := \sqrt{\sum\limits_{i, j=1}^{n, m} a_{ij}^2} = \sqrt{\text{Tr}(A^T A)}$
    - Notice that $||A||_F = ||O^T AO||_F$
  - Condition number for the norm $||\ ||$: $\text{cond}(A) = ||A^{-1}||\cdot ||A||$
    - For a symmetric (or more generally normal) and inversible matrix, the condition number for the L2 norm is the ratio between the largest absolute value of its eigenvalues and the smallest value of its eigenvalues
- Triangular systems: $Ax = b$, where $x \in \mathbb{R}^n$ is unknown, $A \in \mathbb{R}^{n \times n}$, and $b \in \mathbb{R}^n$ is given:
  $$
  A =
  \begin{bmatrix}
  a_{11} & a_{12} & \dots  & a_{1n} \\
         & a_{22} & \dots  & a_{2n} \\
         &        & \ddots & \vdots \\
  0      &        &        & a_{nn}
  \end{bmatrix}
  $$
- Backwards substitution method (for upper triangular systems)
  - Algorithm:
    $$
    \begin{align*}
    &\text{for} \; i \; \text{in} \; n,\ \dots,\ 1 \; \text{do}:\\
    &\qquad x_i = \frac{b_i - \sum\limits_{j=i+1}^n a_{ij}x_j}{a_ii}
    \end{align*}
    $$
- Triangularisable matrices:
  - *Invertible operations*: let $P \in \mathbb{R}^{n \times n}$ be an invertible matrix: ${x: Ax = b} \Leftrightarrow {x: PAx = Pb}$
- Gaussian Elimination
  - Use sequence of invertible operations $P_1,\ \dots,\ P_n$ such that:
      $$
      P_n \cdots P_1\ A = U
      $$
      being $U$ an upper triangular matrix. Then solve:
      $$
      Ux = P_n \cdots P_1\ b
      $$
    - $A^1 = A$, $A^{k+1} = P_{k}A^{k}$, where $a_{ij}^k = 0$ for $1 \leq j \leq k$ and $i \geq j + 1$
      $$
      A^k =
      \begin{bmatrix}
      a_{11}^k & a_{12}^k & a_{13}^k   & \dots  & a_{1n}^k    \\
      0        & \ddots   & \vdots     & \vdots & a_{2n}^k    \\
      \vdots   & 0        & a_{kk}^k   & \vdots & \vdots      \\
      \vdots   & 0        & a_{k+1k}^k & \dots  & a_{k+1n}^k  \\
      \vdots   & \vdots   & \vdots     & \vdots & \vdots      \\
      0        & 0        & a_{nk}^k   & \dots  & a_{nn}^k
      \end{bmatrix}
      $$
    - $P_k = I - v_ke_k^T$, where $e_k = (0, \dots,\ 1_{kth},\ 0,\ \dots,\ 0)\in \mathbb{R}^n$ is the *k*th unit coordinate vector and $v_k = (0, \dots,\ 0,\ \frac{a_{k+1k}^k}{a_{kk}^k}_{(k+1)th},\ \dots,\ \frac{a_{nk}^k}{a_{kk}^k})$
      - $P_k^{-1} = I + v_ke_k^T$ (invertible)
      - $P_{k-1}^{-1}P_k^{-1} = I + v_ke_k^T + v_{k-1}e_{k-1}^T$ (compositions are lower triangular)
  - Choosing a pivot:
    - Default: choose $a_{kk}$ as the pivot
    - Partial pivot: on column $k$, it is chosen the element below the diagonal with the largest absolute value: $i_{pivot} = \arg\max\limits_{i\geq k}|a_{ik}|$
    - Total pivot: choose the largest element below or to the right of the diagonal: $(i_{pivot}, j_{pivot}) = \arg\max\limits_{i, j\geq k}|a_{ij}|$
  - Gaussian elimination gives a triangular decomposition
    - $(P_{n}\cdots P_1)^{-1} = P_{1}^{-1}\cdots P_n^{-1} := L$<br/>
      - LU decomposition: Let $A\in\mathbb{R}^{n\times n}$ be an invertible matrix such that the sub-matrix $A_{1:k,1:k}$ is invertible for $k=1,\ \dots,\ n$. Then the LU decomposition exists: $A = LU$. If $L_{ii} = 1$ is enforced, then de decomposition is unique
  - Each system $Ax = b$ can be solved with two triangular solves:
      $$
      \begin{align*}
      \text{First lower triangular solve:}\; Ly&=b\\
      \text{Second upper triangular solve:}\; Ux&=y
      \end{align*}
      $$
- Gauss Jordan method for inversion: Gaussian elimination to compute the inverse of A
  $$
  \begin{gather*}
  Ax := I\\
  P_{k} \cdots P_{1}\ A = I \\
  P_{k} \cdots P_{1}\ Ax = x = P_{k} \cdots P_{1}\ I = A^{-1}
  \end{gather*}
  $$
- Cholesky decomposition
  - $A$ is positive definite if $v^T Av > 0,\ \forall v \neq 0$
    - M is positive definite if and only if it is symmetric, and all of its eigenvalues are positive
    - For positive definite matrices, it is possible to compute an LU decomposition with $L = U^T$
  - $A\in\mathbb{R}^{n\times n}$ symmetric positive definite matrix. There exists a lower triangular matrix $B\in\mathbb{R}^{n\times n}$ such that $A = BB^T$
    - $b_{jj}=\sqrt{v_j}=\sqrt{a_{jj}-\sum\limits_{k=1}^{j-1}b_{jk}^2}$
    - $\det(A) = (b_{11}\cdots b_{nn})^2$
  - Algorithm:
    $$
    \begin{align*}
    &\text{for} \; j \; \text{in} \; 1,\ \dots,\ n \; \text{do}:\\
    &\qquad\text{Calculate} \; v = a_{:j} - \sum\limits_{k=1}^{j-1} b_{jk}b_{:k}\\
    &\qquad\text{Set} \; b_{:j} = \frac{v}{\sqrt{v_j}}
    \end{align*}
    $$

# Eigenvalues and singular values

- $x \neq 0 \in \mathbb{R}^n$ is an eigenvector with associated eigenvalue $\lambda\in\mathbb{R}$ of $A$ if $Ax = \lambda x \Leftrightarrow (A - \lambda I)x = 0$
  - Consequently $(A - \lambda I)$ is not invertible: $\det (A - \lambda I) = 0$
    - The eigenvalues of A are distinct if and only if $\det (A - \lambda I) = 0$ has n solutions
  - Theorem (Abel-Ruffini): there is no exact algebraic formula for the roots of a polynomial with degree 5 or more
- Let $A \in \mathbb{R}^{n \times n}$, $x \in \mathbb{R}^n$ and $\lambda \in \mathbb{C}$: $x$ is eigenvector and $\lambda$ an eigenvalue of $A$ if $x\neq 0$ and $Ax = \lambda x$
  - $(x, \lambda)$ is referred to as an eigenpair of $A$
  - $\lambda(A)\subset\mathbb{C}$ is the spectrum of $A$ if $\lambda(A)$ contains all the eigenvalues of A, that is: $\lambda(A) := \{\lambda | \exists x\in\mathbb{R}^n \; \text{such that} \; x\neq 0, Ax = \lambda x\}$
  - $A$ is invertible if $0\notin \lambda(A)$
  - If $A = \text{diag}(a_1,\ \dots,\ a_n)$ then $\lambda(A) = \{a_1,\ \dots,\ a_n\}$
  - If $O\in\mathbb{R}^{n\times n}$ is an orthogonal matrix then every $\lambda\in\lambda(O)$ is such that $|\lambda| = 1$
  - Similarity transform: $A\in\mathbb{R}^{n\times n}$ is similar to $B\in\mathbb{R}^{n\times n}$ if there exists such a $P\in\mathbb{R}^{n\times n}$ invertible such that $A = P^{-1}BP$. $A$ is diagonalizable for $B$ diagonal matrix
    - If $A,B\in\mathbb{R}^{n\times n}$ are similar matrices then $\lambda(A)=\lambda(B)$
    - Sufficient but not necessary conditions for $A$ to be diagonalizable
      - $A$ is a normal matrix
      - The eigenvalues of $A$ are distinct
- Spectral theorem for symmetric matrices: symmetric matrices are diagonalizable. That is, let $A\in\mathbb{R}^{n\times n}$, with $A=A^T$. Then there exists an orthogonal matrix $V\in\mathbb{R}^{n\times n}$ and $\Lambda = \text{diag}(\lambda_1,\ \dots,\ \lambda_n) \in\mathbb{R}^{n\times n}$ such that $\Lambda=V^T AV$
- Theorem (singular value decomposition): let $A\in\mathbb{R}^{m\times n}$. There exists orthogonal matrices $U\in \mathbb{R}^{m\times m}$ and $V\in\mathbb{R}^{n\times n}$ such that $U^T AV = \Sigma = \text{diag}(\sigma_1,\ \dots,\ \sigma_{p})$, where $p = \min\{n, m\}$ and $\sigma_1\geq\sigma_2\geq\dots\geq\sigma_p\geq 0$
- Jacobi Method
  - Iteratively minimize off-diagonal elements
    1. Find largest off diagonal element $a_{pq} = \max\limits_{1\leq i < j\leq n}|a_{ij}|$
    2. Replace $a_{pq}$ by a zero using similarity transformations: use the Givens/Jacobi Transform for this
  - Offset: $\text{off}(A) = \sum\limits_{i=0}^n\sum\limits_{j\neq i} a_{ij}^2 = ||A||_F^2 - \sum\limits_{i=1}^n a_{ii}^2$
  - Givens/Jacobi Transform for $c=\cos(\theta)$ and $s=\sin(\theta)$. Carefully choosing $\theta$ and applying Jacobi similar transform $B = J(p,q,\theta)^T AJ(p,q,\theta)$ eliminates $a_{pq}$ (and $a_{qp}$ because of symmetry)
    $$
    \begin{gather*}
    \begin{array}{l}
    \qquad\qquad\qquad\qquad\qquad\qquad\quad &&p&&q&&
    \end{array} \\
    \theta\in\bigg[-\frac{\pi}{4}, \frac{\pi}{4}\bigg]\setminus\{0\},\ J(p, q, \theta) =
    \begin{bmatrix}
    1      & \dots  & 0      & \dots  & 0      & \dots  & 0      \\
    \vdots & \ddots & \vdots &        & \vdots &        & \vdots \\
    0      & \dots  & c      & \dots  & s      & \dots  & 0      \\
    \vdots &        & \vdots & \ddots & \vdots &        & \vdots \\
    0      & \dots  & -s     & \dots  & c      & \dots  & 0      \\
    \vdots &        & \vdots &        & \vdots & \ddots & \vdots \\
    0      & \dots  & 0      & \dots  & 0      & \dots  & 1
    \end{bmatrix}
    \begin{array}{c}
    \\ \\ p \\ \\ q \\ \\ \\
    \end{array}
    \end{gather*}
    $$
    - Thus:
      $$
      \begin{align*}
      i,j \not\in\{p,q\},\ &b_{ij} = b_{ji} = a_{ij}          \\
      j\not\in\{p, q\},\ &b_{pj} = b_{jp} = ca_{pj} - sa_{qj} \\
      j\not\in\{p, q\},\ &b_{qj} = b_{jq} = ca_{pj} + sa_{qj} \\
      &b_{pp} = a_{pp} - ta_{pq}                      \\
      &b_{qq} = a_{qq} + ta_{pq}                      \\
      &b_{pq} = b_{qp} = 0
      \end{align*}
      $$
    - Algorithm:
      $$
      \begin{align*}
      & x = \frac{a_{qq}-a_{pp}}{2a_{pq}} \\
      & t = \min\{|-x+\sqrt{x^2+1}|,|-x-\sqrt{x^2+1}|\} \\
      & c = \frac{1}{\sqrt{1+t^2}} \\
      & s = ct
      \end{align*}
      $$
  - Algorithm:
    $$
    \begin{align*}
    &\text{Initialize}: \; k=0 \; \text{and} \; A^0 = A \\
    &\text{while} \; \text{off}(A^{k+1})<\epsilon\; \text{do}: \\
    &\qquad\text{Choose} \; (p,q) \; \text{so that} \; a_{pq} = \max\limits_{1\leq i < j\leq n}|a_{ij}| \\
    &\qquad(c, s) = \; \text{Calculate Jacobi Transform}((p, q, A^k)) \\
    &\qquad A^{k+1} = J(p, q, \theta)^T A^k J(p, q, \theta)
    \end{align*}
    $$
  - Lemma
    - $J(p,q,\theta)$ is an orthogonal matrix
      - $O = \begin{bmatrix} c & s \\ -s & c \end{bmatrix}$ is an orthogonal matrix
    - Let $||A||_F^2 = \text{Tr}(A^T A)$ and let $J$ be an orthogonal matrix. Consequently $||J^TAJ||_F^2=||A||_F^2$
  - Theorem: $\text{off}(A^k)\leq\big(1-\frac{2}{n(n-1)}\big)^k\text{off}(A)$

# Linear Programming

- Fundamental theorem of linear programming: Let $P=\{x|Ax=b,x\geq 0\}$ then either
  - (Bounded feasible) $P\neq\{\varnothing \}$ and there exists a vertex $v$ of $P$ such that $v\in\arg\max\limits_{x\in P}c^T x$
  - (Infeasible region) $P=\{\varnothing\}$
  - (Unbounded feasible region) There exists $x, d \in \mathbb{R}^n$ such that $x+td\in P$ for all $t\geq 0$ and $\lim\limits_{t\rightarrow\infty}c^T(x+td)=\infty$
- Problem notation
  - $n$ variables and $m$ constraints
  - The linear objective function $z=\sum\limits_{j=1}^n c_j x_j$
  - The $m$ inequality constraints in standard form $\sum\limits_{j=1}^n a_{ij}x_j \leq b_i,\;\text{for}\; i\in\{1,\ \dots,\ m\}$
    - Transform to standard form
      - Replace $\sum\limits_{j=1}^n a_{ij}x_j = b_i$ by $\sum\limits_{j=1}^n a_{ij}x_j \leq b_i$ and $-\sum\limits_{j=1}^n a_{ij}x_j \leq -b_i$
      - Replace $\alpha\leq x$ by $y=x-\alpha,\ y\geq 0$
      - Replace $x\leq \beta$ by $y=\beta-x,\ y\geq 0$
      - Replace $\alpha\leq x \leq \beta$ by $y=x-\alpha,\ y\leq \beta - \alpha,\ y\geq 0$
  - The $n$ positivity constraints $x_j \geq 0\; \text{for}\; j\in\{1,\ \dots,\ n\}$
  - $x_i^*$ denotes the value of the ith variable
  - $(x_i^*,\ \dots,\ x_n^*)\in\mathbb{R}^n$ a feasible solution if it satisfies the inequality and positivity constraints
- Dictionary notation
  - The slack variables $(x_{n+1},\ \dots,\ x_{n+m})\in\mathbb{R}^m$
  - The initial dictionary
    $$
    \begin{align*}
    &x_i = b_i - \sum\limits_{j=1}^n a_{ij}x_j,\; \text{for}\; i\in I \\
    &z=\sum\limits_{j=1}^n c_j x_j
    \end{align*}
    $$
  - Valid dictionary if $m$ of the variables $(x_{1}^*,\ \dots,\ x_{n+m}^*)$ can be expressed as function of the remaining $n$ variables
  - The $m$ variables on the left-hand side are the basic variables
  - The $n$ variables on the right-hand side are the non-basic variables
  - After row elimination operations, a new basis is obtained
    - Basic variable set $I\subset\{1,\ \dots,\ n+m\}$ and non-basic set $J\subset\{1,\ \dots,\ n+m\}\setminus I$, with $|I| = m$ and $|J| = n$
    - Current objective value $z^*=\sum\limits_{j=1}^n c_j x_j^*$
    - For each basis set I there is a corresponding dictionary
      $$
      \begin{align*}
      &x_i = b_i' - \sum\limits_{j=1}^n a_{ij}'x_j,\; \text{for}\; i\in I \\
      &z=z^*+\sum\limits_{j=1}^n c_j' x_j
      \end{align*}
      $$
      where $a_{ij}', b_i', z^*\in\mathbb{R}$ are coefficients resulting from the row operations
        - For this to be a feasible dictionary, it is required that $b_i'\geq 0$
      - Basic solution: $x_i^*=b_i'\; \text{for}\; i\in I$ and $x_j^*=0\; \text{for}\; j\in J$
- Simplex method
  - Algorithm
    $$
    \begin{align*}
    &\text{if} \; c_i'\leq 0\; \text{for all} \;i\in J \; \text{then}: \\
    &\qquad\text{STOP}\qquad \text{\# Optimal point found} \\
    &\text{Choose a variable} \; j_0 \; \text{to enter the basis from the set}\; j_0\in\{j\in J:c_j' > 0\} \\
    &\text{if} \; a_{ij_0}'\leq 0\; \text{for all} \;i\in J \; \text{then}: \\
    &\qquad\text{STOP}\qquad \text{\# The problem is unbounded} \\
    &\text{Choose a variable} \; i_0 \; \text{to leave the basis from the set}\; i_0\in\arg\min\limits_{i\in I, a_{ij_0}'>0}\frac{b_i'}{a_{ij_0}'} \\
    &I\leftarrow(I\setminus\{i_0\}) \; \text{and} \; J\leftarrow J\cup\{i_0\}\qquad \text{\# Move}\; i_0 \;\text{from basic to non-basic} \\
    &\text{for} \; i\in I \; \text{do}:\\
    &\qquad a_{i:}'\leftarrow a_{i:}'-\frac{a_{ij_0}'}{a_{i_0j_0}'}a_{i_0:}'\qquad \text{\# Row elimination on pivot} \; (i_0, j_0) \\
    &a_{i_0:}'\leftarrow \frac{1}{a_{i_0j_0}'}a_{i_0:}' \; \text{and} \; a_{i_0j_0}'\leftarrow \frac{1}{a_{i_0j_0}'}\qquad \text{\# Normalize the coefficient of}\; a_{i0j0}' \\
    &c'\leftarrow c'- \frac{c_{j_0}'}{a_{i_0j_0}'}a_{i_0:}'\qquad \text{\# Update the cost coefficients} \\
    &I\leftarrow I\cup\{j_0\} \; \text{and} \; J\leftarrow (J\setminus\{j_0\})\qquad \text{\# Move}\; j_0 \;\text{from non-basic to non-basic} \\
    \end{align*}
    $$
  - How to choose who enters the basis? $j_0\in\{j\in J:c_j' > 0\}$
    - Mad hatter rule: randomly
    - Dantzig's 1st rule: $j_0=\arg\max\limits_{j\in J}c_j$
    - Dantzig's 2st rule: choose $j_0$ that maximizes the increase in z: $j_0=\arg\max\limits_{j\in J}\bigg\{c_j\min\limits_{i\in I,\ a_{ij} > 0}\frac{b_i}{a_{ij}}\bigg\}$
    - Bland's rule: choose the smallest indices $j_0$ and $i_0$:
      $$
      \begin{gather*}
      j_0 = \arg\min\{j\in J: c_j > 0\} \\
      i_0 = \min\bigg\{\arg\min\limits_{i\in I,\ a_{ij} > 0}\frac{b_i}{a_{ij_0}}\bigg\}
      \end{gather*}
      $$
      - Degeneracy
        - If any of the basic variables are zero, then the solution is degenerate
        - Theorem: if Bland's rule is used on all degenerate dictionaries, then the simplex algorithm will not cycle
- Finding an initial feasible dictionary
  - Set up an auxiliary problem
    $$
    \begin{align*}
    &x_i = b_i - \sum\limits_{j=1}^n a_{ij}x_j + x_0, \; \text{for}\; i\in I \\
    &w = -x_0
    \end{align*}
    $$
    - Pivot on most infeasible variable in the basis with the most negative value; continue with simplex algorithm until a feasible basis without $x_0$ is found; then, remove column with $x_0$ and replace $w$ with $z$ (eliminating base variables from z)
- Upper bounds to check progress
  - The LP in standard form
    $$
    \begin{align*}
    \max\limits_x z : &= c^Tx \\
    \text{subject to}\; Ax &\leq b, \\
    x &\geq 0
    \end{align*}
    \qquad\text{Primal (P)}
    $$
    - $b_i$ total quantity of the resource $i$
    - $a_ij$ amount of resource $i$ consumed by producing one unit of product $j$
    - $c_j$ profit brought by one unit of product j
    - $x_j$ manufactured quantity of product $j$
  - Find upper bound: find $w\in\mathbb{R}$ so that $z=c^Tx\leq w$ for all $x\in\mathbb{R}^n$. For an upper bound as tight as possible: suppose $y\geq 0\in\mathbb{R}^m$ so that $y^TA\geq c^T$ and $c^Tx\leq (y^TA)x \leq y^Tb = : w$. Consequently:
    $$
    \begin{align*}
    \min\limits_y w : &= y^Tb \\
    \text{subject to}\; A^Ty &\geq c, \\
    y &\geq 0
    \end{align*}
    \qquad\text{Dual (D)}
    $$
    - $y_i$ minimum price where it makes sense to sell resource $i$ directly rather than manufacturing products
  - Lemma (weak duality): If $x\in\mathbb{R}^n$ is a feasible point for ($P$) and $y\in\mathbb{R}^m$ is a feasible point for ($D$) then $c^Tx\leq (y^TA)x \leq y^Tb$
    - If ($P$) has an unbounded solution, then there exist no feasible point $y$ for ($D$)
    - If ($D$) has an unbounded solution, then there exist no feasible point $x$ for ($P$)
    - if $x$ and $y$ are primal and dual feasible, respectively, and $c^Tx=y^Tb$, then $x$ and $y$ are the primal and dual optimal points, respectively
  - Theorem (strong duality: checking for optimality): if ($P$) or ($D$) is feasible, then $z^*=w^*$. Moreover, if $c^*$ is the cost vector of the optimal dictionary of the primal problem ($P$) then $y_i^*=-c_{n+i}^*$ is the optimal dual solution for $i=1,\ \dots,\ m$
    - Thus the distance to optimal is given by $w-z=y^Tb-c^Tx\geq 0$
- Theorem (complementary slackness): a feasible solution $x^*$ is optimal if there exists $y^*$, an optimal solution for the dual problem, such that
  $$
  \begin{align*}
  \forall i \in \{1,\ \dots,\ m\}, \; &\text{if} \; \sum\limits_{j=1}^n a_{ij}x_j^* < b_i, \; \text{then} \; y_i^* = 0 \\
  \forall j \in \{1,\ \dots,\ n\}, \; &\text{if} \; x_j^* > 0, \; \text{then} \; \sum\limits_{i=1}^m a_{ij}y_i^* = c_j
  \end{align*}
  $$
- A small variation $\delta b$ of $b$ leads to a variation of $z$ equal to $\delta b\cdot y$, where $y$ is an optimal solution of ($D$).

# Nonlinear programming

- Minimize a nonlinear differentiable function $f:x\in\mathbb{R}^n\mapsto f(x)\in\mathbb{R}$: $x^*=\arg\min\limits_{x\in\mathbb{R}^n}f(x)$
  - This problem is often impossible: first check there exists a minimum; develop iterative methods $x^1,\ \dots,\ x^k,\ \dots$ so that $\lim\limits_{k\rightarrow\infty}x^k=x^*$
    - Template method: $x^{k+1}=x^k+s^kd^k$ where $s^k>0$ is a step size and $d^k\in\mathbb{R}^n$ is search direction. Satisfy the descent condition $f(x^{k+1})<f(x^k)$
- Definition of local minimum: The point $x^*\in\mathbb{R}^n$ is a local minimum of $f(x)$ if there exists $r>0$ such that $f(x^*)\leq f(x),\forall||x-x^*||_2<r$
- Definition of global minimum: The point $x^*\in\mathbb{R}^n$ is a global minimum of $f(x)$ if $f(x^*)\leq f(x),\forall x$
- Multivariate calculus
  - For a differentiable function $f:x\in\mathbb{R}^n\mapsto f(x)\in\mathbb{R}$, the gradient evaluated at $x$ is $\nabla f(x)=\bigg[\frac{\partial f(x)}{\partial x_1},\ \dots,\ \frac{\partial f(x)}{\partial x_n}\bigg]^T$
    - Note that $\nabla f(x)$ is a column-vector
  - For any vector valued function $F:x\in\mathbb{R}^n\mapsto F(x)=[f_1(x),\ \dots,\ f_n(x)]^T\in\mathbb{R}^n$ define the Jacobian matrix by
    $$
    \nabla F(x) =
    \begin{bmatrix}
    \frac{\partial f_1(x)}{\partial x_1} & \dots  & \frac{\partial f_n(x)}{\partial x_1}\\
    \vdots                               & \ddots & \vdots \\
    \frac{\partial f_1(x)}{\partial x_n} & \dots  & \frac{\partial f_n(x)}{\partial x_n}
    \end{bmatrix}
    =[\nabla f_1(x),\ \dots, \nabla f_n(x)]
    $$
    - 1st order Taylor $f(x^0 + d) = f(x^0)+ \nabla f(x^0)^Td+\epsilon(d)\ ||d||_2$ where $\epsilon(d)$ is a real valued such that $\lim\limits_{d\rightarrow 0}\epsilon(d) = 0$
      - Definition of limit: given any constant $c > 0$ there exists $\delta > 0$ such that $||d|| < \delta \Rightarrow |\epsilon(d)| < c$
  - Hessian matrix: if $f\in C^2$, the Hessian matrix is
    $$
    \nabla^2 f(x) :=
    \begin{bmatrix}
    \frac{\partial^2 f_1(x)}{\partial x_1\partial x_1} & \dots  & \frac{\partial^2 f_n(x)}{\partial x_1\partial x_n} \\
    \vdots                                             & \ddots & \vdots \\
    \frac{\partial^2 f_1(x)}{\partial x_n\partial x_1} & \dots  & \frac{\partial^2 f_n(x)}{\partial x_n\partial x_n}
    \end{bmatrix}
    $$
    - if $f\in C^2$ then $\frac{\partial^2 f_1(x)}{\partial x_i\partial x_j}=\frac{\partial^2 f_1(x)}{\partial x_j\partial x_i},\ \forall i,j\in\{1,\ \dots,\ n\}\Leftrightarrow \nabla^2 f(x) = \nabla^2 f(x)^T$
      - 2nd order Taylor $f(x^0 + d) = f(x^0)+ \nabla f(x^0)^Td + \frac{1}{2}d^T\nabla^2 f(x^0)d + \epsilon(d)\ ||d||_2^2$ where $\epsilon(d)$ is a real valued such that $\lim\limits_{d\rightarrow 0}\epsilon(d) = 0$
      - Product rule: the vector valued version of the product rule
        - For any function $F(x):\mathbb{R}^n\mapsto\mathbb{R}^n$ and matrix $A\in\mathbb{R}^{n\times n}$, $\nabla(F(x)^TA)=\nabla F(x)^TA$
        - For any two vector valued functions $F_1$ and $F_2$, $\nabla(F_1(x)^TF_2(x)) = \nabla F_1(x)^TF_2(x) + \nabla F_2(x)^TF_2(x)$
- How to choose $d$?
  - Lemma (steepest descent): for $d\in\mathbb{R}^n$ the local change of $f(x)$ around $x^0$ is $\Delta(d):=\lim\limits_{s\rightarrow 0^+}\frac{f(x^0+sd)-f(x^0)}{s}$. Let $v=-\frac{\nabla f(x^0)}{||\nabla f(x^0)||_2}$. We have $v=\arg\min\limits_{d\in\mathbb{R}^n}\Delta(d)$ subject to $||d||_2 = 1$
    - Corollary (descent condition): if $d^T\nabla f(x^0)<0$ then there exists $s>0$ such that $f(x^0+sd)<F(x^0)$
  - Theorem (necessary optimality conditions): If $x^*$ is a local minimum of $f(x)$ then $\nabla f(x^*) = 0$ and $d^T\nabla^2 f(x^*)d \geq 0,\ \forall d\in\mathbb{R}^n$ (i.e. $\nabla^2 f(x^*)$ is positive definite)
  - Theorem (sufficient optimality conditions): If $x^*$ is such that $\nabla f(x^*) = 0$ and $d^T\nabla^2 f(x^*)d > 0,\ \forall d\in\mathbb{R}^n\; \text{with} \; d\neq 0$ (i.e. $\nabla^2 f(x^*)$ is positive semi-definite) then $x^*$ is a local minimum
  - Quadratic functions: $f(x)=x^TAx-x^Tb+c$, $A$ symmetric positive definite
    - Convex: Only has one global minimum which must be the global minimum
  - Convex functions: $f(tx + (1 - t)y)\leq tf(x) + (1-t)f(y),\ \forall x, y\in\mathbb{R}^n,\ t\in [0, 1]$
    - Hessian matrix is positive semidefinite
  - Theorem: if $f$ is a convex function, then every local minimum of $f$ is also a global minimum
  - Theorem: if $f$ is twice continuously differentiable, then the following three statements are equivalent:
    - $f(tx + (1 - t)y)\leq tf(x) + (1-t)f(y),\ \forall x, y\in\mathbb{R}^n,\ t\in [0, 1]$
    - $f(y)\geq f(x) + \nabla f(x)^T(y-x),\ \forall x, y\in\mathbb{R}^n$
    - $0\leq d^T\nabla^2 f(x)^Td,\ \forall x, d \in\mathbb{R}^d$
- How to choose $s$?
  - For quadratic functions, chosen a fixed step size of $s^k = \frac{1}{\sigma_{\max}(A)}$, then GD converges $\because ||\nabla f(x^{k+1})||_2\leq\bigg(1 - \frac{\sigma_{\min}(A)}{\sigma_{\max}(A)}\bigg)^k||\nabla f(x^0)||_2$
  - For non-quadratic functions:
    - (zig-zagging convergence) $s^k=\arg\min\limits_{s\geq 0}f(x^k+sd^k)$
    - Backtracking line search algorithm:
      $$
      \begin{align*}
      &\text{Choose} \; \alpha > 0,\ p, c \in (0,1) \\
      &\text{while} \; f(x^k+\alpha d^k) \leq f(x^k) + c\alpha\nabla f(x^k)^T \; \text{do}: \\
      &\qquad\text{Update} \; \alpha=\rho\alpha
      \end{align*}
      $$
    - Gradient descent algorithm:
      $$
      \begin{align*}
      &\text{Choose} \; x^0\in\mathbb{R}^n \\
      &\text{while} \; ||f(x^k)||_2 > \epsilon \; \text{or} \; f(x^{k-1})-f(x^k) \leq \epsilon \; \text{do}: \\
      &\qquad\text{Calculate} \; d^k=-\nabla f(x^k) \\
      &\qquad\text{Calculate} \; s^k \; \text{using Backtracking line search} \\
      &\qquad\text{Update} \; x^{k+1} = x^k +s^kd^k
      \end{align*}
      $$
- Newton's method
  - Minimizes local quadratic approximation (2nd Taylor)
  - $x^{k+1} = x^k - \nabla^2 f(x^k)^{-1}\nabla f(x^k)$
  - Theorem: let $f(x)$ be a $\mu$-strongly convex function: $v^T\nabla^2 f(x)v\geq\mu||v||^2$. If the Hessian is also Lipschitz $||\nabla^2 f(x) - \nabla^2 f(y)||_2\leq L||x-y||_2$ then the Newton's method converges according to $||x^{k+1}-x^*||_2\leq\frac{L}{2\mu}||x^k-x^*||_2^2$. In particular, if $||x^0-x^*||_2\leq\frac{\mu}{L}$, then for $k\geq1$, $||x^k-x^*||_2\leq\frac{\mu}{2^{2^k}L}$
- Constrained nonlinear optimization
  - Let $f, g_i, h_j\in C^1$, for $i\in\{1,\ \dots,\ m\}=I$ and $j\in\{1,\ \dots,\ p\}=J$. Consider the constrained optimization problem:
    $$
    \begin{align*}
    \min\limits_{x\in\mathbb{R}^n} f(x)& \\
    \text{subject to}\; g_i(x) &\leq 0,\; \text{for} \; i\in I\qquad\text{Inequality constraints} \\
    h_j(x) &= 0,\; \text{for} \; j\in J\qquad\text{Equality constraints} \\
    \end{align*}
    $$
    - Feasible point $x$: satisfies all constraints
    - Feasible set X: all the feasible points: $X:=\{x\in\mathbb{R}^n:g_i()x\leq 0, h_j(x)=0,\; \text{for}\; i\in I, j\in J \}$
    - Abbreviated form: $\min\limits_{x\in X}f(x)$
  - Theorem (existence): if the feasible set $X$ is bounded and non-empty, then there exists a solution to $\min\limits_{x\in X}f(x)$
  - Definition: $f:\mathbb{R}^n\mapsto\mathbb{R}$ is coercive if $\lim\limits_{||x||\rightarrow\infty}f(x)=\infty$
  - Theorem: If $X$ is non-empty and $f$ is coercive, then there exists a solution to $\min\limits_{x\in X}f(x)$
  - Definition: we say that $d$ is an admissible direction at $x_0\in X$ if there exists a $C^1$ differentiable curve $\phi:\mathbb{R}_+\mapsto\mathbb{R}^n$ such that $\phi(0)=x_0$ and $\phi'(0)=d$. We denote by $A(x_0)$ the set of admissible directions at $x_0$
    - Lemma: Let $\phi:\mathbb{R}_+\mapsto\mathbb{R}^n$ be a $C^1$ curve, $f:\mathbb{R}^n\mapsto\mathbb{R}$ be continuously differentiable. Then the first order Taylor expansion of the composition $f(\phi(t))$ around $x_0$ can be written as $f(\phi(t))=f(x_0)+t d^T\nabla f(x_0)+t\hat{\epsilon}(t)$ where $\lim\limits_{t\rightarrow 0}\hat{\epsilon}(t) = 0$
  - Theorem (necessary condition for admissible direction): let $I_0(x_0)=\{i: g_i(x_0)=0,i\in I\}$ be the indexes of saturated inequalities. If $d\in A(x_0)$ is an admissible direction, then:
    - For every $i\in I(x_0)$ we have that $d^T\nabla g_i(x_0)\leq 0$
    - For every $j\in J$ we have that $d^T\nabla h_j(x_0) = 0$
    - Let $B(x_0)$ be the set of directions, denominated the cone of feasible directions, that satisfy the above two conditions. Thus $A(x_0)\subset B(x_0)$
  - Definition: constraint qualifications hold at $x_0$ if for every $d\in B(x_0)$ there exists a sequence $(d_t)_{t=1}^\infty \in A(x_0)$ such that $d_t\rightarrow d$
  - Theorem (necessary conditions): let $x^*$ be a local minimum. If the constraint qualifications hold at $x^*$ then for every $d\in B(x^*)$, $\nabla f(x^*)^Td\geq 0$. Every direction in the feasible cone is not a descent direction
  - Theorem (Lagrange's condition): let $x^*\in X$ be a local minimum and suppose that the constraint qualifications hold at $x^*$. It follows that the gradient of the objective is a linear combination of the gradients of constraints at $x^*$ that is, there exists $\mu_j\in\mathbb{R}$ for $j\in J$ such that $\nabla f(x^*)=\sum\limits_{j\in J}\mu_j\nabla h_j(x^*)$
  - Theorem (Karush, Kuhn and Tuckers condition): let $x^*\in X$ be a local minimum and suppose that the constraint qualifications hold at $x^*$. It follows that there exists $\mu_j\in\mathbb{R}$ and $\lambda_i\in\mathbb{R}_+$ for $j\in J$ and $i\in I(x^*)$ such that $\nabla f(x^*)=\sum\limits_{j\in J}\mu_j\nabla h_j(x^*) - \sum\limits_{i\in I(x^*)}\lambda_i\nabla g_i(x^*)$
  - Theorem (separating hyperplane theorem): let $X, Y \subset \mathbb{R}^n$ be two disjoint convex sets. Then there exists a hyperplane defined by $v\in\mathbb{R}^n$ and $\beta\in\mathbb{R}$ such that $\langle v, x \rangle \leq \beta\; \text{and}\; \langle v, y \rangle \geq \beta,\ \forall x\in X, \forall y \in Y$
  - Theorem (separating a cone and a point): consider a given vector $b$ and the cone $K:=\{A\lambda+B\mu | \forall\lambda\geq 0,\forall\mu\}$. Then either $b\in K$ or there exists a vector $y$ such that $\langle y, b \rangle\leq 0\;\text{and}\;\langle y, k, \rangle \geq 0,\ \forall k\in K$
  - Theorem (sufficient conditions): let $f$ and $g_i$ for $i\in I$ be convex functions. Let $h_j$ be linear for $j\in J$. Suppose the constraint qualifications hold at $x^*\in X$ and the KKT conditions are verified. Then $x^*$ is a local minimum
- Lagrangian formulation
  - The KKT conditions are often described with the help of an auxiliary function called the Lagrangian function $L(x, \mu, \lambda) := f(x) - \langle \mu, h(x) \rangle + \langle \lambda, g(x) \rangle$, where $h(x):=(h_j)_{j\in J}$ and $g(x):=(g_i)_{i\in I}$
  - Theorem: let $x\in\mathbb{R}^n, \mu\in\mathbb{R}^{|J|}, \lambda\in\mathbb{R}^{|I|}$. The KKT conditions hold if:
    $$
    \begin{gather*}
    \nabla_x L(x, \mu, \lambda) = 0 \\
    \nabla_\mu L(x, \mu, \lambda) = 0 \\
    \nabla_\lambda L(x, \mu, \lambda) \leq 0
    \end{gather*}
    $$
