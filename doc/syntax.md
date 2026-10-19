# syntax

```
polynomial = term { term }
term       = factor { ( '/' | [ '*' ] )  factor }
factor     = power { power }
power      = primary [ '^' number ]
primary    = variable | number | '(' polynomial ')'
variable   = 'a' ... 'z' | 'A' ... 'Z'
number     = [ ( '+' | '-' )] digit { digit }
digit      = '0' ... '9'
```

## 開始条件
### term
variable | '+' | '-' | digit | '('
### factor
variable | '+' | '-' | digit | '('
### power
variable | '+' | '-' | digit | '('
### primary
variable | '+' | '-' | digit | '('

