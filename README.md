# Procore Github SplitView App
By: Haasith Sanka

## API Requests:
Github List of Pull Request:
```
https://api.github.com/repos/{owner}/{repoName}/pulls
```
Github Pull Request Info:
```
https://api.github.com/repos/{owner}/{repoName}/pulls/{PullRequestNumber}
```

## Architecture:
Used Java for logic and xml for UI. Home page has a button that goes to a list of open pull requests and then you can hit on any of the pull requests to see a split view of the file changes for that pull request and you can also see unison view if you want as well there's unison view button on the action bar on the splitView activity.
	
