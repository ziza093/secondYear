import asyncio
import random
from asyncio import Queue

async def gauss_sum(n):
    result = 0
    
    for i in range(n + 1):
        result += i
    
    print(f"sum({n}) = {result}")
    await asyncio.sleep(1)

async def main():

    numbers = Queue()
    #simulating a queue of numbers
    for i in range(4):
        await numbers.put(random.randint(1,50))

    await asyncio.gather(
        gauss_sum(await numbers.get()),
        gauss_sum(await numbers.get()),
        gauss_sum(await numbers.get()),
        gauss_sum(await numbers.get()),
    )

if __name__ == "__main__":
    asyncio.run(main())
    
    