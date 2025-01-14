from flask import Flask, request, jsonify
import subprocess

app = Flask(__name__)

@app.route('/execute', methods=['POST'])
def execute():
    data = request.json
    language = data['language']
    code = data['code']
    filename = f"/workspace/temp_script.{get_extension(language)}"

    # Save the code to a file
    with open(filename, 'w') as f:
        f.write(code)

    # Execute the file
    command = get_command(language, filename)
    try:
        result = subprocess.check_output(command, shell=True, stderr=subprocess.STDOUT, timeout=30)
        return jsonify({'output': result.decode('utf-8')})
    except subprocess.CalledProcessError as e:
        return jsonify({'error': e.output.decode('utf-8')}), 400

def get_extension(language):
    extensions = {
        'python': 'py',
        'javascript': 'js',
        'java': 'java',
        'cpp': 'cpp',
    }
    return extensions.get(language, '')

def get_command(language, filename):
    commands = {
        'python': f"python3 {filename}",
        'javascript': f"node {filename}",
        'java': f"javac {filename} && java -cp /workspace {filename.split('.')[0]}",
        'cpp': f"g++ {filename} -o /workspace/temp && /workspace/temp",
    }
    return commands.get(language, '')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
