

function ProiectbDialogContent() {
    return (

        <div style={{display: 'flex', width: '90%',
            alignItems: 'center', gap: '8px', margin: 'auto', flexDirection: 'column'}}>
            <img
                src="/proiectb_qr.jpg"
                alt="Proiect B QR codes example"
                style={{ width: '50%', objectFit: 'cover', height: 'auto' }}
            />
            <p style={{flex: 1, margin: 0}}>
                Proiect B is an urban intervention project which uses QR codes to create a live stage of dynamic content in Bucharest. Its operating principle is simple: there are two types of QR codes - the harlequin and the key. When someone scans the harlequin code he gets redirected to a web page which has some content: text, images, music; when scanning the key code, the user gets redirected to an admin webpage which allows him to change the active content associated to the harlequin codes. In this way anyone who finds the key code can permanently change the information that other users who scan the harlequin see. Once changed, the new content has a 15 minutes timer before it can be changed again - a subtle reference to Andy Warhol's’ 15 minutes of fame. Stickers of such QR codes are spread in different locations in Bucharest for people to find and send anonymous messages back. Thus static, physical content becomes dynamic in nature once activated by human intervention.
                This project wants to shift the traditional paradigms of communication and blur the borders between real life and digital ways of communication. Its playful character instigates people to interaction and exploration. All content history is saved and represents itself an ever-evolving work.
            </p>
            <a href={'https://cucicov.com'} style={{width: '100%'}}>cucicov.com</a>


        </div>
    )
}

export default ProiectbDialogContent;