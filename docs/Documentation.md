---
  layout: default.md
  title: "TalentMatch Documentation Guide"
  pageNav: 3
---

# TalentMatch Documentation Guide

* We use [**MarkBind**](https://markbind.org/) to manage TalentMatch's documentation.
* The `docs/` folder contains the source files for the TalentMatch documentation website.
* To learn how set it up and maintain the project website, follow the guide [[se-edu/guides] Working with Forked MarkBind sites](https://se-education.org/guides/tutorials/markbind-forked-sites.html) for project documentation.

## Documentation Structure

TalentMatch documentation includes:

* **User Guide**: Targeted at HR professionals and recruiters who will use TalentMatch to manage internship applications.
* **Developer Guide**: For developers who will maintain and extend TalentMatch.
* **UML Diagrams**: Visualize the architecture and class relationships:
  * **Logic Class Diagram**: Shows the command structure and parser relationships.
  * **Model Class Diagram**: Illustrates entity relationships between Person, Job, and Application objects.
  * **Parser Class Diagram**: Details the command parsing hierarchy.
  * **Storage Class Diagram**: Shows how data is persisted between sessions.

## Style Guidance

* Follow the [**_Google developer documentation style guide_**](https://developers.google.com/style).
* Also relevant is the [_se-edu/guides **Markdown coding standard**_](https://se-education.org/guides/conventions/markdown.html).
* Use consistent terminology related to the recruitment domain:
  * Refer to "applicants" or "candidates" (not "users") when describing people applying for jobs
  * Use "HR recruiters" when referring to the users of TalentMatch
  * Maintain consistent naming of entities (Person, Job, Application) as defined in the Model diagram

## UML Diagram Maintenance

When updating TalentMatch's UML diagrams:
* Ensure all diagrams accurately reflect the current architecture
* Include all new classes and methods when adding features
* Maintain proper relationships between components
* Use consistent styling as defined in the `style.puml` file

## Converting to PDF

* See the guide [_se-edu/guides **Saving web documents as PDF files**_](https://se-education.org/guides/tutorials/savingPdf.html).
* PDF versions of the documentation are especially useful for distributing TalentMatch's user guide to HR professionals.
