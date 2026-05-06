export default function LangTag(props) {

    const { color, language } = props;

    return (
        <div className="lang-tag-main" style={{ backgroundColor: color }}>{language}</div>
    )
}