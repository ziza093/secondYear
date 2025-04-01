#include <iostream>
#include <vector>
#include <chrono>
#include <algorithm>

void comparaSiSchimba(int &a, int &b, bool ascending) {
    if ((ascending && a > b) || (!ascending && a < b)) {
        std::swap(a, b);
    }
}

void sortareSecventaBitona(std::vector<int> &arr, int i, int d, bool ascending) {
    if (d == 2) {
        comparaSiSchimba(arr[i], arr[i + 1], ascending);
    } else {
        for (int j = 0; j < d / 2; j++) {
            comparaSiSchimba(arr[i + j], arr[i + j + d / 2], ascending);
        }
        sortareSecventaBitona(arr, i, d / 2, ascending);
        sortareSecventaBitona(arr, i + d / 2, d / 2, ascending);
    }
}

void BatcherSort(std::vector<int> &arr, int i, int d, bool ascending) {
    if (d == 2) {
        comparaSiSchimba(arr[i], arr[i + 1], ascending);
    } else {
        BatcherSort(arr, i, d / 2, true);
        BatcherSort(arr, i + d / 2, d / 2, false);
        sortareSecventaBitona(arr, i, d, ascending);
    }
}

void masoaraTimpul(std::vector<int> &arr, const std::string &descriere) {
    auto start = std::chrono::high_resolution_clock::now();
    BatcherSort(arr, 0, arr.size(), true);
    auto stop = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> durata = stop - start;
    std::cout << descriere << " - Timp: " << durata.count() << " secunde" << std::endl;
}

int main() {
    const int n = 1024 * 1024; // Putere a lui 2 pentru compatibilitate
    std::vector<int> sorted(n);
    std::vector<int> reversed(n);
    std::vector<int> random(n);

    for (int i = 0; i < n; i++) {
        sorted[i] = i;
        reversed[i] = n - i;
        random[i] = rand() % n;
    }
    
    std::cout << "Măsurare timp pentru n = " << n << " elemente:\n";
    masoaraTimpul(sorted, "Vector deja sortat");
    masoaraTimpul(reversed, "Vector sortat descrescător");
    masoaraTimpul(random, "Vector aleator");
    
    return 0;
}