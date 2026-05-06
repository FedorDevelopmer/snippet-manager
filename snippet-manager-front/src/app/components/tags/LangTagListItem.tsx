
export default function LangTagListItem(props) {

    const { color, language, onClick } = props;

    return (
        <div className="lang-tag-edit" onClick={onClick}>
            <div className="tag-input" style={{ backgroundColor: color }}></div>
            <label className="tag-label">{language}</label>
        </div>
    );
}