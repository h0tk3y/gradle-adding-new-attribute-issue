# gradle-adding-new-attribute-issue

### How to run the reproducer

In `producer`:

```bash
./gradlew publish
./gradlew publish -Pv2=true
```

Then in `consumer`:

```bash
./gradlew resolve # Should succeed
./gradlew resolve -Pv2=true # Should fail
```