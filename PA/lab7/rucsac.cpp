#include <iostream>
#include <vector>
using namespace std;

struct stare {
	int w;
	int p;
};

vector<stare> interclasare(vector<stare> S, vector<stare> T)
{
	vector<stare> Sn;
	Sn.push_back(S[0]);

	int j = 1;
	int k = 0;
	int lp = 0;

	while (j < S.size() && k < T.size())
	{
		if (S[j].w < T[k].w)
		{
			if (S[j].p > lp)
			{
				Sn.push_back(S[j]);
				lp = S[j].p;
			}
			j++;
		}
		else if (T[k].w < S[j].w)
		{
			if (T[k].p > lp)
			{
				Sn.push_back(T[k]);
				lp = T[k].p;
			}
			k++;
		}
		else
		{
			if (T[k].p > lp && T[k].p > S[j].p)
			{
				Sn.push_back(T[k]);
				lp = T[k].p;
			}
			else if (S[j].p > lp && S[j].p > T[k].p)
			{
				Sn.push_back(S[j]);
				lp = S[j].p;
			}
			j++;
			k++;
		}
	}
	while (j < S.size())
	{
		if (S[j].p > lp)
		{
			Sn.push_back(S[j]);
			lp = S[j].p;
		}
		j++;
	}
	while (k < T.size())
	{
		if (T[k].p > lp)
		{
			Sn.push_back(T[k]);
			lp = T[k].p;
		}
		k++;
	}
  
	return Sn;
}

void rucsac(int M, int n, int w[], int p[], int x[])
{
	vector<stare> S[n+1];
	vector<stare> T[n+1];
	
	stare temp = {w: 0, p: 0};
	S[0].push_back(temp);
	
	temp.w = w[0]; temp.p = p[0];
  	T[0].push_back(temp);

	// inserati codul aici
    for (int i = 0; i < n; i++) {
        
        for (stare s : S[i]) {
            if (s.w + w[i] <= M) { 
                T[i].push_back({s.w + w[i], s.p + p[i]});
            }
        }

        S[i+1] = interclasare(S[i], T[i]);
    }

    //calcul profit maxim
    int profitMax = 0;
    for (stare s : S[n]) {
        if (s.p > profitMax) {
            profitMax = s.p;
        }
    }

    int aux = profitMax;
    
    for(int i=n-1; i>=0; i--){

        int stop = 0;

        for(stare elem : S[i]){
            if(elem.p == aux){
                stop=1;
                break;
            }
            
        }

        if(stop == 0){
            x[i] = 1;
            aux -= w[i];
        }
    }
    
    cout << "Profit maxim: " << profitMax << "\n";
}

int main()
{
	int M = 10;
	int n = 3;
	int w[] = {3, 5, 6};
	int p[] = {10, 30, 20};
	int x[] = {0, 0, 0};
	
  	rucsac(M, n, w, p, x);
	
    for(int elem : x){
        cout<<elem<<" ";
    }
    
    cout<<"\n";

	return 0;
}