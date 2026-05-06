export default function LangTag(props : any) {

    const { color, language } = props;

    return (
        <div className="lang-tag-main" style={{ backgroundColor: color }}>{language}</div>
    )
}