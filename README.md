# Pharmacy Management and Dispensing Module

### [Github Actions](https://github.com/features/actions)

For the RwandaEMR project, we have begun to establish the use of Github Actions for CI automation.  
Github Actions are configured on a per-repository basis, and contain those workflows that you wish to configure for 
that repository.  The advantage of this platform for the RwandaEMR project is that anyone who has write-access to the 
repository can create or edit a CI workflow.

Anyone can also easily view all of the details of each workflow within the code project itself.  
See the [workflows defined here](https://github.com/Rwanda-EMR/openmrs-module-pharmacymanagement/tree/master/.github/workflows) 
for how this is configured for this project.

## Standard Workflows

### verify-prs:

For every pull request that is issued against the configured branches (currently just master), 
this job runs a “mvn verify”, which compiles, tests, and verifies that the there are no errors.

### deploy-snapshots:

For every commit pushed to the configured branches (currently master), this job runs a “mvn deploy”, which compiles, 
executes tests, and verifies that this builds without errors, and if so, deploys to the OpenMRS Maven Snapshots repository 
that is configured in the distributionManagement section of 
the [pom.xml](https://github.com/Rwanda-EMR/openmrs-module-pharmacymanagement/blob/master/pom.xml)

### perform-release:

This job exists to perform a versioned, non-SNAPSHOT release.  
This is not automatically executed,  but rather is initiated by an explicit, authenticated REST request.  
This job runs the following maven goals:

```mvn release:prepare```
- Removes “-SNAPSHOT” from pom.xml files and commits
- Tags this commit with the version number
- Increments the version in the pom.xml to the next snapshot version, and commits this

```mvn release:perform```
- Checks out the tag with the version created in the release:prepare step
- Builds the code artifacts
- Deploys these to the OpenMRS Maven repository as configured in pom distributionManagement section

```mvn deploy```
- Builds the next snapshot version created at the end of release:prepare
- Deploys this to the OpenMRS Maven snapshots repository that is configured in the pom

To initiate the release workflow one would take the following steps:
* [Create a personal access token](https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line) to authenticate into Github with minimal permissions (public_repo and write:repo_hook)
* In a terminal, set the GITHUB_TOKEN environment variable to this token value:
```export GITHUB_TOKEN=areallylongtokenthatisgeneratedingithub```
* Issue a request to hit the deployment endpoint:
```
curl  -H "Authorization: token $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github.everest-preview+json"  \
      -H "Content-Type: application/json" \
      --request POST \
      --data '{"event_type": "perform-release"}' \
      https://api.github.com/repos/Rwanda-EMR/openmrs-module-pharmacymanagement/dispatches
```
