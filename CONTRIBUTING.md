# Contributing to Karya

## Linting and Formatting

- [Detekt](https://detekt.dev/) Plugin is being used to enforce code style and formatting
- This is part of the build step hence ensure `./gradlew detekt` runs successfully for the build to succeed.
- Ruleset can be found [here](./detekt.yml)

### Intellij Config

Additionally in Intellij, configure the following:

**Set the indentation to space : 2**
![indentation_settings](./docs/media/intellij_indentation.png)

**While running the Intellij Formatter, check the below options**
![format_settings](./docs/media/intellij_format.png)
