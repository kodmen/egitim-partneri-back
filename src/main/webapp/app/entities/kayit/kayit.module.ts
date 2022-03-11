import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KayitComponent } from './list/kayit.component';
import { KayitDetailComponent } from './detail/kayit-detail.component';
import { KayitUpdateComponent } from './update/kayit-update.component';
import { KayitDeleteDialogComponent } from './delete/kayit-delete-dialog.component';
import { KayitRoutingModule } from './route/kayit-routing.module';

@NgModule({
  imports: [SharedModule, KayitRoutingModule],
  declarations: [KayitComponent, KayitDetailComponent, KayitUpdateComponent, KayitDeleteDialogComponent],
  entryComponents: [KayitDeleteDialogComponent],
})
export class KayitModule {}
