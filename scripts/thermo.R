linFit <- function(df) {
    lm(Cp ~ t, df)
}

quadFit <- function(df) {
    lm(Cp ~ t + I(t^2) + I(t^(-2)), df)
}

shomateFit <- function(df) {
    lm(Cp ~ t + I(t^2) + I(t^(3)) + I(t^(-2)), df)
}

quadFit1 <- function(df) {
    lm(Cp ~ t + I(t^2) + I(t^(-2)), df)
}

quadFit2 <- function(df) {
    lm(Cp ~ t + I(t^(-2)), df)
}

plotFit <- function(fit, df, tmin = min(df$t), tmax = max(df$t)) {
    curve(predict(fit, data.frame(t)), tmin, tmax, xname="t")
    points(df, col="red", pch=19)
}

prepareVars <- function(fit, t) {
    coeffs <- coefficients(fit)
    coeffs <- c(coeffs, rep(0L, 4 - length(coeffs)))
    vars <- as.list(setNames(coeffs * c(1, 1e3, 1e6, 1e-6),
                             c("a", "b", "c", "e")))
    c(vars, list(f = 0, g = 0, d = 0, t = t/1000))
}

doH <- function(vars) {
    with(vars, a * t + b * t^2 / 2 + c * t^3 / 3 + d * t^4 / 4 - e / t + f)
}

H <- function(a, b, c, d, e, f, g, t = 298) {
    t <- t/1000
    doH(mget(names(formals(sys.function()))))
}


enthalpy <- function(fit, t, H = 0) {
    vars <- prepareVars(fit, t)
    vars$f <- H - computeH(local({
        vars$t <- 298/1000
        vars
    }))
    computeH(vars)
}

doS <- function(vars) {
    with(vars, a * log(t) + b * t + c * t^2 / 2 + d * t^3 / 3 - e / (2*t^2) + g)
}

S <- function(a, b, c, d, e, f, g, t = 298) {
    t <- t/1000
    doS(mget(names(formals(sys.function()))))
}

entropy <- function(fit, t, S, tref = 298) {
    vars <- prepareVars(fit, t)
    vars$g <- S - computeS(local({
        vars$t <- tref/1000
        vars
    }))
    computeS(vars)
}

langeT <- c(298, 400, 600, 800, 1000)
langeDf <- function(Cp, t = head(langeT, length(Cp))) {
    data.frame(t, Cp)
}

entropyFromEnthalpyDown <- function(S0, Hf, T) {
    S0 - Hf * 1000 / T
}

enthalpyFusionFromEntropyChange <- function(S0, S1, T) {
    (S1-S0)*T
}

entropyFromEnthalpyUp <- function(S0, Hf, T) {
    Hf * 1000 / T + S0
}

df <- langeDf(c(96.4, 108, 121))
fit <- quadFit(df)
plotFit(fit, df)

entropy(fit, 3306, 32.6)
enthalpy(fit, 607, H=-495)

entropyFromEnthalpyUp(88.3, 41.6, 2041)

`
