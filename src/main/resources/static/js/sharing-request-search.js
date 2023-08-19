function fetchUserSearchResults(query) {
    var resultsDiv = document.getElementById('sharing-request-search-results');

    fetch(`/sharing/request/user-search?search=${query}`)
        .then(response => response.json())
        .then(data => {
            if (data && data.length) {
                resultsDiv.innerHTML = data.map(
                    item => `
                        <div>
                            <a href="/sharing/request/make?receiverId=${item.userId}"> ${item.displayName} </a>
                        </div>
                    `).join('');
            } else {
                resultsDiv.innerHTML = '<div>No results found</div>';
            }
            resultsDiv.style.display = 'block';
        })
        .catch(err => {
            console.error("Error fetching results:", err);
            resultsDiv.innerHTML = '<div>Error fetching results</div>';
            resultsDiv.style.display = 'block';
        });
};

function initializeSharingRequestUserSearch() {
    var searchInput = document.getElementById('sharing-request-search-input');
    var resultsDiv = document.getElementById('sharing-request-search-results');

    searchInput.addEventListener('focus', () => {
        if (searchInput.value) {
            fetchUserSearchResults(searchInput.value);
        } else {
            resultsDiv.innerHTML = '<div>Please enter a keyword</div>';
            resultsDiv.style.display = 'block';
        }
    });

    searchInput.addEventListener('input', (event) => {
        if (event.target.value) {
            fetchUserSearchResults(event.target.value);
        } else {
            resultsDiv.innerHTML = '<div>Please enter a keyword</div>';
            resultsDiv.style.display = 'block';
        }
    });

    searchInput.addEventListener('blur', () => {
        setTimeout(() => {
            resultsDiv.style.display = 'none';
        }, 200);
    });
}
