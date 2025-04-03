---
  layout: default.md
  title: "TalentMatch Configuration Guide"
---

# TalentMatch Configuration Guide

Certain properties of TalentMatch can be controlled (e.g user preferences file location, logging level) through the configuration file (default: `config.json`).

## Configuration File Structure

TalentMatch's configuration file contains the following key properties:

```json
{
  "appTitle" : "TalentMatch",
  "logLevel" : "INFO",
  "userPrefsFilePath" : "preferences.json",
  "addressBookFilePath" : "data/addressbook.json",
  "applicationsManagerFilePath" : "data/applicationsmanager.json"
}
```

## Configuration Options

### Application Settings

* `appTitle`: The title displayed in the application window (default: "TalentMatch")
* `logLevel`: The minimum logging level to display (options: "SEVERE", "WARNING", "INFO", "FINE", "FINER", "FINEST")

### Data Storage Settings

* `userPrefsFilePath`: Path to the user preferences file
* `addressBookFilePath`: Path to the file storing candidate information
* `applicationsManagerFilePath`: Path to the file storing job applications data

## Modifying Configuration

### When to Modify Configuration

* **Development**: Change logging levels for debugging
* **Deployment**: Configure custom data storage locations
* **Testing**: Use separate data files to isolate test environments

### How to Modify Configuration

1. Open `config.json` in any text editor
2. Modify the desired properties
3. Save the file
4. Restart TalentMatch for changes to take effect

### Example: Development Configuration

```json
{
  "appTitle" : "TalentMatch - Development",
  "logLevel" : "FINE",
  "userPrefsFilePath" : "preferences-dev.json",
  "addressBookFilePath" : "data/addressbook-dev.json",
  "applicationsManagerFilePath" : "data/applicationsmanager-dev.json"
}
```

### Example: Production Configuration

```json
{
  "appTitle" : "TalentMatch",
  "logLevel" : "WARNING",
  "userPrefsFilePath" : "preferences.json",
  "addressBookFilePath" : "data/addressbook.json",
  "applicationsManagerFilePath" : "data/applicationsmanager.json"
}
```

## Best Practices for TalentMatch Configuration

* Maintain separate configurations for development and production environments
* Increase logging detail when troubleshooting recruitment data issues
* Use consistent file paths across deployments
* Always back up your configuration file before making changes
* Ensure the configured file paths are accessible to the application
