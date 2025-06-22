import subprocess
import shlex

def execute_pipeline(command):
    commands = [cmd.strip() for cmd in command.split('|')]
    
    if not commands:
        print("The command isnt valid.")
        return
        
    for i, cmd in enumerate(commands):
        print(f"  {i+1}. {cmd}")
    
    previous_process = None
    processes = []
    
    for i, cmd in enumerate(commands):
        args = shlex.split(cmd)
        
        if i == 0:
            process = subprocess.Popen(
                args,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )
        elif i == len(commands) - 1:
            process = subprocess.Popen(
                args,
                stdin=previous_process.stdout,
                text=True
            )
        else:
            process = subprocess.Popen(
                args,
                stdin=previous_process.stdout,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )
        
        if previous_process:
            previous_process.stdout.close()
        
        previous_process = process
        processes.append(process)
    
    output, error = processes[-1].communicate()
    
    exit_codes = [p.wait() for p in processes]
    
    print("Result:")
    if output:
        print(output)

def main():
    print("Exemplu: ip a | grep inet | wc -l")
    
    while True:
        command = input("\nWrite a command or 'exit' to quit: ")
        
        if command.lower() == 'exit':
            break
            
        try:
            execute_pipeline(command)
        except Exception as e:
            print(f"Error: {e}")

if __name__ == "__main__":
    main()