# Definition-Use and CFG Analysis 

**Considering the [writeBrokenWord()](../src/main/java/org/davidmoten/text/utils/WordWrap.java#602) method
within the [WordWrap](../src/main/java/org/davidmoten/text/utils/WordWrap.java) class for DU and CFG Analysis**

---

## Method

```java
private static void writeBrokenWord(LineConsumer out, StringBuilder2 word, boolean insertHyphens) throws IOException {
        // to be really thorough we'd check the new stringWidth with '-' but let's not
        // bother for now
        String x;
        if (insertHyphens && word.length() > 2
                && !isWhitespace((x = word.substring(0, word.length() - 2)))) {
            out.write(x);
            out.write("-");
            out.writeNewLine();
            word.delete(0, word.length() - 2);
        } else {
            String prefix = word.substring(0, word.length() - 1);
            if (!isWhitespace(prefix)) {
                out.write(prefix);
            }
            out.writeNewLine();
            word.delete(0, word.length() - 1);
        }
    }
```

---

## CFG Analysis

- Compound condition involving `&&`, `if/else`, and a nested `else` condition

## DU Analysis 

**Map the following variables for the DU chain**: 
- `x` - defined inside the compound condition -> used when the hyphen-insertion branch executes
- `prefix` - defined and used in the `else` branch
- `word` - contents are read and then modified using `delete()` 
- `insertHyphens` - boolean parameter used to determine which branch is taken

### Table

| Variable | Definition (D) | Use (U)  | DU Chain           | Anomaly Type          |
|----------|----------------|----------|--------------------|-----------------------|
| `x`      | Line 605       | Line 608 | D605(x) -> U608(x) | None                  |