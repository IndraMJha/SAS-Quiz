import csv
import json
import os

def convert_csv_to_firebase_json(csv_dir, output_file):
    firebase_data = {"questions": {}}

    for filename in os.listdir(csv_dir):
        if filename.endswith(".csv"):
            paper_name = os.path.splitext(filename)[0]
            firebase_data["questions"][paper_name] = {}

            file_path = os.path.join(csv_dir, filename)
            with open(file_path, mode='r', encoding='utf-8') as csv_file:
                # Assuming no header. If there is a header, use next(csv_reader)
                csv_reader = csv.reader(csv_file)

                for i, row in enumerate(csv_reader):
                    if len(row) < 6:
                        print(f"Skipping row {i} in {filename}: insufficient columns")
                        continue

                    question_id = f"q{i+1}"
                    question_text = row[0]
                    options = [row[1], row[2], row[3], row[4]]
                    answer = row[5]

                    firebase_data["questions"][paper_name][question_id] = {
                        "text": question_text,
                        "options": options,
                        "answer": answer
                    }

    with open(output_file, 'w', encoding='utf-8') as json_file:
        json.dump(firebase_data, json_file, indent=2)

    print(f"Successfully created {output_file}")

if __name__ == "__main__":
    # Change 'app/src/main/assets' if your CSVs are elsewhere
    assets_dir = "app/src/main/assets"
    output_json = "quiz_data.json"

    if os.path.exists(assets_dir):
        convert_csv_to_firebase_json(assets_dir, output_json)
        print("Now you can import 'quiz_data.json' into your Firebase Realtime Database.")
    else:
        print(f"Error: Directory '{assets_dir}' not found. Please update the path in the script.")
