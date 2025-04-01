#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

struct Item {
    int weight, profit;
};

void knapsack(int M, int n, vector<Item>& items, vector<int>& x) {
    vector<vector<int>> dp(n + 1, vector<int>(M + 1, 0));
    
    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= M; w++) {
            if (items[i - 1].weight <= w) {
                dp[i][w] = max(dp[i - 1][w], dp[i - 1][w - items[i - 1].weight] + items[i - 1].profit);
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }
    
    int w = M;
    for (int i = n; i > 0; i--) {
        if (dp[i][w] != dp[i - 1][w]) {
            x[i - 1] = 1;
            w -= items[i - 1].weight;
        } else {
            x[i - 1] = 0;
        }
    }
    
    cout << "Profit maxim: " << dp[n][M] << "\n";
    cout << "Obiectele selectate: ";
    for (int i = 0; i < n; i++) {
        if (x[i]) cout << i + 1 << " ";
    }
    cout << "\n";
}

int main() {
    int M = 10;
    vector<Item> items = {{3, 10}, {5, 30}, {6, 20}};
    int n = items.size();
    vector<int> x(n, 0);
    
    knapsack(M, n, items, x);
    return 0;
}
