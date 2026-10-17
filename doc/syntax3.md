# syntax

```
expression     = c-polynomial
c-polynomial   = [ '-' ] c-term { ( '+' | '-') c-term }
c-term         = [ int ] { c-factor }
c-factor       = '(' polynomial ')' [ '^' int ]
polynomial     = [ '-' ] term { ( '+' | '-' ) term }
term           = [ int ] { factor }
factor         = variable [ '^' int ]
```

## 事前条件

poly-mult内でpolynomialが連続することがある。
term内でfactorが連続することがある。
これらはループを継続する条件を明確にする必要がある。

### polynomial
int または Variable または '(' で始まっている。

### factor
Variable または '(' で始まっている。